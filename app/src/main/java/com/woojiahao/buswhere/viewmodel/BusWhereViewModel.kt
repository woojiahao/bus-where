package com.woojiahao.buswhere.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woojiahao.buswhere.models.Stop
import com.woojiahao.buswhere.models.search
import com.woojiahao.buswhere.repository.BusWhereDataState
import com.woojiahao.buswhere.repository.BusWhereRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BusWhereUiState(
  val dataState: BusWhereDataState = BusWhereDataState.Loading,
  val filteredFavorites: List<Stop> = emptyList(),
  val filteredOthers: List<Stop> = emptyList(),
  val searchQuery: String = ""
)

class BusWhereViewModel(private val repository: BusWhereRepository) : ViewModel() {
  private val searchQuery = MutableStateFlow("")

  val uiState: StateFlow<BusWhereUiState> = combine(
    repository.bundle, repository.favoriteStops, searchQuery
  ) { dataState, favStops, query ->
    val q = query.trim().lowercase()
    val stops = (dataState as? BusWhereDataState.Success)?.bundle?.stops ?: emptyList()
    val (favStops, otherStops) = stops.partition { it.busStopCode in favStops }

    BusWhereUiState(
      dataState = dataState,
      filteredFavorites = favStops.search(q),
      filteredOthers = otherStops.search(q),
      searchQuery = query
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = BusWhereUiState()
  )

  init {
    viewModelScope.launch { repository.refresh() }
  }

  fun onSearchChange(query: String) {
    searchQuery.value = query
  }

  fun toggleFavorite(stop: Stop) {
    viewModelScope.launch { repository.toggleFavorite(stop.busStopCode) }
  }

  fun refresh() {
    viewModelScope.launch { repository.refresh() }
  }
}