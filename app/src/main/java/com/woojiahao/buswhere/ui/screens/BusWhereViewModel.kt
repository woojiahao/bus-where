package com.woojiahao.buswhere.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woojiahao.buswhere.data.Bundle
import com.woojiahao.buswhere.data.Service
import com.woojiahao.buswhere.data.Stop
import com.woojiahao.buswhere.network.BusWhereApi
import com.woojiahao.buswhere.network.models.BusWhereRoute
import com.woojiahao.buswhere.network.models.BusWhereService
import com.woojiahao.buswhere.network.models.BusWhereStop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException

sealed interface BusWhereUiState {
    data class Success(val bundle: Bundle) : BusWhereUiState
    object Error : BusWhereUiState
    object Loading : BusWhereUiState
}

class BusWhereViewModel : ViewModel() {
    var uiState: BusWhereUiState by mutableStateOf(BusWhereUiState.Loading)
        private set

    private var cachedBundle: Bundle = Bundle()
    private var currentQuery: String = ""

    init {
        loadData()
    }

    fun loadData(forceRefresh: Boolean = false) {
        if (!cachedBundle.isEmpty() && !forceRefresh) {
            uiState = BusWhereUiState.Success(cachedBundle)
        }

        viewModelScope.launch {
            uiState = BusWhereUiState.Loading

            try {
                val result = withContext(Dispatchers.IO) {
                    val servicesRequest = async { BusWhereApi.retrofitService.getServices() }
                    val stopsRequest = async { BusWhereApi.retrofitService.getStops() }
                    val routesRequest = async { BusWhereApi.retrofitService.getRoutes() }

                    createBundle(
                        servicesRequest.await(), stopsRequest.await(), routesRequest.await()
                    )
                }
                cachedBundle = result
                uiState = BusWhereUiState.Success(result)
            } catch (e: IOException) {
                uiState = BusWhereUiState.Error
            }
        }
    }

    private fun createBundle(
        apiServices: List<BusWhereService>,
        apiStops: List<BusWhereStop>,
        apiRoutes: List<BusWhereRoute>
    ): Bundle {
        // Every row in apiServices corresponds to service that can run for multiple routes (depending on the direction)
        // Every row in apiRoutes corresponds to a bus stop on the route, distinguished by { serviceNo, direction }
        // Every row in apiStops corresponds to a bus stop

        // Routes are distinguished by { serviceNo, direction } since each service can go in at most
        // two directions.
        // Services are also distinguished by { serviceNo, direction }
        // Ultimately, we are interested in Service }|--|{ Stop, the route does not provide much
        // information for BusWhere
        // Alternatively, we can merge data from Route into Service, so that { serviceNo, direction }
        // references a single route
        val apiStopsByBusStopCode = apiStops.associateBy { it.busStopCode }
        val routes = apiRoutes.groupBy { Service.Key(it.serviceNo, it.direction) }.mapValues {
            it.value.sortedWith(
                compareBy { t -> t.stopSequence })
                .mapNotNull { t -> apiStopsByBusStopCode[t.busStopCode] }
        }
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
        val stopsByBusStopCode = stops.associateBy { it.busStopCode }
        val services = apiServicesByKey.map {
            Service(key = it.key, stops = it.value.map { t -> stopsByBusStopCode[t.busStopCode]!! })
        }

        return Bundle(services, stops)
    }
}