package com.woojiahao.buswhere.repository

import com.woojiahao.buswhere.models.Bundle
import kotlinx.coroutines.flow.Flow

sealed interface BusWhereDataState {
  data object Loading : BusWhereDataState
  data object Error : BusWhereDataState
  data class Success(val bundle: Bundle) : BusWhereDataState
}

interface BusWhereRepository {
  val bundle: Flow<BusWhereDataState>
  val favoriteStops: Flow<Set<Int>>
  suspend fun refresh()
  suspend fun toggleFavorite(busStopCode: Int)
}
