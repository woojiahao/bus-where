package com.woojiahao.buswhere.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.woojiahao.buswhere.models.Service
import com.woojiahao.buswhere.models.Stop
import com.woojiahao.buswhere.repository.BusWhereArrivalState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BusStopList(
  filteredFavorites: List<Stop>,
  filteredOthers: List<Stop>,
  stopServices: Map<Int, List<Service>>,
  arrivalState: BusWhereArrivalState,
  onToggleFavorite: (stop: Stop) -> Unit,
  onFetchArrivals: (busStopCode: Int) -> Unit,
  onSelectService: (service: Service, stop: Stop) -> Unit,
  isWidgetConfigMode: Boolean = false,
) {
  var expandedStopCode by rememberSaveable { mutableStateOf<Int?>(null) }
  val isEmpty = filteredFavorites.isEmpty() && filteredOthers.isEmpty()

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 16.dp)
  ) {
    if (filteredFavorites.isNotEmpty()) {
      item {
        SectionHeader(
          title = "Favorites",
          icon = Icons.Default.Star,
        )
      }

      itemsIndexed(
        items = filteredFavorites,
        key = { _, stop -> "fav_${stop.busStopCode}" }
      ) { index, stop ->
        if (isWidgetConfigMode && stopServices[stop.busStopCode] != null) {
          BusStopWidgetSelectionRow(
            stop = stop,
            stopServices = stopServices[stop.busStopCode]!!,
            isExpanded = expandedStopCode == stop.busStopCode,
            isFavorite = true,
            onToggleExpand = {
              expandedStopCode =
                if (expandedStopCode == stop.busStopCode) null else stop.busStopCode
            },
            onSelectService = { service -> onSelectService(service, stop) },
            modifier = Modifier.animateItem(),
          )
        } else {
          BusStopArrivalsRow(
            stop = stop,
            isExpanded = expandedStopCode == stop.busStopCode,
            isFavorite = true,
            arrivalState = arrivalState,
            onToggleFavorite = onToggleFavorite,
            onToggleExpand = {
              expandedStopCode =
                if (expandedStopCode == stop.busStopCode) null else stop.busStopCode
            },
            onFetchArrivals = onFetchArrivals,
            modifier = Modifier.animateItem(),
          )
        }
      }
    }

    item {
      SectionHeader(
        title = if (filteredOthers.isEmpty()) "All" else "Others",
        icon = Icons.Default.LocationOn,
      )
    }

    if (isEmpty) {
      item(key = "empty") {
        EmptyState()
      }
    } else {
      itemsIndexed(
        items = filteredOthers,
        key = { _, stop -> "stop_${stop.busStopCode}" }
      ) { index, stop ->
        if (isWidgetConfigMode && stopServices[stop.busStopCode] != null) {
          BusStopWidgetSelectionRow(
            stop = stop,
            stopServices = stopServices[stop.busStopCode]!!,
            isExpanded = expandedStopCode == stop.busStopCode,
            isFavorite = false,
            onToggleExpand = {
              expandedStopCode =
                if (expandedStopCode == stop.busStopCode) null else stop.busStopCode
            },
            onSelectService = { service -> onSelectService(service, stop) },
            modifier = Modifier.animateItem(),
          )
        } else {
          BusStopArrivalsRow(
            stop = stop,
            isExpanded = expandedStopCode == stop.busStopCode,
            isFavorite = false,
            arrivalState = arrivalState,
            onToggleFavorite = onToggleFavorite,
            onToggleExpand = {
              expandedStopCode =
                if (expandedStopCode == stop.busStopCode) null else stop.busStopCode
            },
            onFetchArrivals = onFetchArrivals,
            modifier = Modifier.animateItem()
          )
        }
      }
    }
  }
}