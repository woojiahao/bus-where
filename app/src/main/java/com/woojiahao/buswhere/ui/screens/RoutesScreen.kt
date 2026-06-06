package com.woojiahao.buswhere.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RoutesScreen(
  busWhereUiState: BusWhereUiState,
  modifier: Modifier = Modifier
) {
  when (busWhereUiState) {
    is BusWhereUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
    is BusWhereUiState.Success -> ResultScreen(
      busWhereUiState.bundle.services.joinToString("\n") {
        "${it.key.serviceNo} going ${it.key.direction} has ${it.stops.size} stops"
      },
      modifier.fillMaxWidth()
    )

    is BusWhereUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
  }
}

@Composable
private fun ResultScreen(results: String, modifier: Modifier) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    Text(results)
  }
}