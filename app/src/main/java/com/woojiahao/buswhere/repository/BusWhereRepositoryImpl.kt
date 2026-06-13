package com.woojiahao.buswhere.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.woojiahao.buswhere.data.api.BusWhereApiDataSource
import com.woojiahao.buswhere.data.api.dtos.BusWhereApiRouteDto
import com.woojiahao.buswhere.data.api.dtos.BusWhereApiServiceDto
import com.woojiahao.buswhere.data.api.dtos.BusWhereApiStopDto
import com.woojiahao.buswhere.models.Arrival
import com.woojiahao.buswhere.models.Bundle
import com.woojiahao.buswhere.models.Service
import com.woojiahao.buswhere.models.Stop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BusWhereRepositoryImpl(
  private val apiDataSource: BusWhereApiDataSource,
  private val context: Context
) : BusWhereRepository {
  private val Context.dataStore by preferencesDataStore(name = "favorites")
  private val FAV_KEY = stringSetPreferencesKey("favorite_bus_stop_codes")

  private val _bundle = MutableStateFlow<BusWhereDataState>(BusWhereDataState.Loading)
  override val bundle: Flow<BusWhereDataState>
    get() = _bundle.asStateFlow()

  override val favoriteStops: Flow<Set<Int>>
    get() = context.dataStore.data.map {
      it[FAV_KEY]?.mapNotNull { t -> t.toIntOrNull() }?.toSet() ?: emptySet()
    }

  override suspend fun refresh() {
    _bundle.value = BusWhereDataState.Loading
    _bundle.value = try {
      withContext(Dispatchers.IO) {
        val servicesDeferred = async { apiDataSource.getServices() }
        val stopsDeferred = async { apiDataSource.getStops() }
        val routesDeferred = async { apiDataSource.getRoutes() }
        val bundle = createBundle(
          servicesDeferred.await(),
          stopsDeferred.await(),
          routesDeferred.await()
        )
        BusWhereDataState.Success(bundle)
      }
    } catch (e: Exception) {
      BusWhereDataState.Error
    }
  }

  override suspend fun toggleFavorite(busStopCode: Int) {
    context.dataStore.edit { prefs ->
      val current = prefs[FAV_KEY]?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
      val updated = if (busStopCode in current) current - busStopCode else current + busStopCode
      prefs[FAV_KEY] = updated.map { it.toString() }.toSet()
    }
  }

  override suspend fun getArrivals(busStopCode: Int): Flow<BusWhereArrivalState> = flow {
    emit(BusWhereArrivalState.Loading)
    try {
      val apiArrivals = withContext(Dispatchers.IO) { apiDataSource.getArrivals(busStopCode) }
      val arrivals = apiArrivals.map { (serviceNo, nextBus1, nextBus2, nextBus3) ->
        Arrival(
          serviceNo = serviceNo,
          nextBus1 = Arrival.Bus(
            load = nextBus1.load,
            estimatedArrival = nextBus1.estimatedArrival
          ),
          nextBus2 = Arrival.Bus(
            load = nextBus2.load,
            estimatedArrival = nextBus2.estimatedArrival
          ),
          nextBus3 = Arrival.Bus(
            load = nextBus3.load,
            estimatedArrival = nextBus3.estimatedArrival
          ),
        )
      }
      emit(BusWhereArrivalState.Success(arrivals))
    } catch (e: Exception) {
      emit(BusWhereArrivalState.Error)
    }
  }

  private fun createBundle(
    apiServices: List<BusWhereApiServiceDto>,
    apiStops: List<BusWhereApiStopDto>,
    apiRoutes: List<BusWhereApiRouteDto>
  ): Bundle {
    // Every row in apiServices corresponds to service that can run for multiple routes (depending on the direction)
    // Every row in apiRoutes corresponds to a bus stop on the route, distinguished by { serviceNo, direction }
    // Every row in apiStops corresponds to a bus stop

    // { busStopCode => api stop }
    val apiStopsByBusStopCode = apiStops.associateBy { it.busStopCode }

    // { (serviceNo, direction) => [api stop] } where [api stop] is the sorted bus stops for the
    // given route
    val routes = apiRoutes
      .groupBy { Service.Key(it.serviceNo, it.direction) }
      .mapValues {
        it.value.sortedWith(
          compareBy { t -> t.stopSequence })
          .mapNotNull { t -> apiStopsByBusStopCode[t.busStopCode] }
      }

    // { (serviceNo, direction) => api service }
    val apiServicesByKey = apiServices.associate {
      val key = Service.Key(it.serviceNo, it.direction)
      key to routes[key]!!
    }

    val stops = apiStops.map {
      Stop(
        description = it.description,
        latitude = it.latitude,
        longitude = it.longitude,
        busStopCode = it.busStopCode,
        roadName = it.roadName,
      )
    }

    // { busStopCode => stop }
    val stopsByBusStopCode = stops.associateBy { it.busStopCode }

    val services = apiServicesByKey.map {
      Service(key = it.key, stops = it.value.map { t -> stopsByBusStopCode[t.busStopCode]!! })
    }

    return Bundle(services, stops)
  }
}