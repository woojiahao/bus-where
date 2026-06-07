package com.woojiahao.buswhere.repository

import com.woojiahao.buswhere.models.Arrival
import com.woojiahao.buswhere.models.Bundle
import kotlinx.coroutines.flow.Flow

sealed interface BusWhereDataState {
  data object Loading : BusWhereDataState
  data object Error : BusWhereDataState
  data class Success(val bundle: Bundle) : BusWhereDataState
}

sealed interface BusWhereArrivalState {
  data object Idle : BusWhereArrivalState
  data object Loading : BusWhereArrivalState
  data object Error : BusWhereArrivalState
  data class Success(val arrivals: List<Arrival>) : BusWhereArrivalState
}

interface BusWhereRepository {
  val bundle: Flow<BusWhereDataState>
  val favoriteStops: Flow<Set<Int>>
  suspend fun refresh()
  suspend fun toggleFavorite(busStopCode: Int)
  suspend fun getArrivals(busStopCode: Int): Flow<BusWhereArrivalState>
}
