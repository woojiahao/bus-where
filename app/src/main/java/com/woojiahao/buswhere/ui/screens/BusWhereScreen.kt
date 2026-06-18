package com.woojiahao.buswhere.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.woojiahao.buswhere.models.Service
import com.woojiahao.buswhere.models.Stop
import com.woojiahao.buswhere.repository.BusWhereDataState
import com.woojiahao.buswhere.ui.components.BusStopList
import com.woojiahao.buswhere.ui.components.ErrorState
import com.woojiahao.buswhere.viewmodel.BusWhereViewModel

@Preview()
@Composable
fun BusWhereScreen(
  modifier: Modifier = Modifier,
  vm: BusWhereViewModel = viewModel(),
  showSearchBar: Boolean = false,
  isWidgetConfigMode: Boolean = false,
  onSelectService: (service: Service, stop: Stop) -> Unit = { _, _ -> }
) {
  val state by vm.uiState.collectAsStateWithLifecycle()

  Column(modifier = modifier.fillMaxSize()) {
    when (state.dataState) {
      BusWhereDataState.Error -> ErrorState(onRetry = vm::refresh)
      BusWhereDataState.Loading -> Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator()
      }

      is BusWhereDataState.Success -> BusStopList(
        filteredFavorites = state.filteredFavorites,
        filteredOthers = state.filteredOthers,
        stopServices = state.stopServices,
        arrivalState = state.arrivalState,
        searchBarEnabled = true,
        showSearchBar = showSearchBar,
        searchQuery = state.searchQuery,
        onSearchChange = vm::onSearchChange,
        onToggleFavorite = vm::toggleFavorite,
        onFetchArrivals = vm::fetchArrivals,
        onSelectService = onSelectService,
        isWidgetConfigMode = isWidgetConfigMode
      )
    }
  }
}