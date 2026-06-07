package com.woojiahao.buswhere.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.woojiahao.buswhere.models.Stop

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BusStopList(
  filteredFavorites: List<Stop>,
  filteredOthers: List<Stop>,
  onToggleFavorite: (stop: Stop) -> Unit,
  modifier: Modifier = Modifier
) {
  val isEmpty = filteredFavorites.isEmpty() && filteredOthers.isEmpty()

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 16.dp)
  ) {
    if (filteredFavorites.isNotEmpty()) {
      stickyHeader(key = "header_favorites") {
        SectionHeader(
          title = "Favorites",
          icon = Icons.Default.Star,
          modifier = modifier,
        )
      }

      items(
        items = filteredFavorites,
        key = { "fav_${it.busStopCode}" }
      ) { stop ->
        BusStopRow(
          stop = stop,
          isFavorite = true,
          onToggleFavorite = onToggleFavorite,
          modifier = Modifier.animateItem(),
        )
      }
    }

    stickyHeader(key = "header_all") {
      SectionHeader(
        title = if (filteredOthers.isEmpty()) "All" else "Others",
        icon = Icons.Default.LocationOn,
        modifier = modifier,
      )
    }

    if (isEmpty) {
      item(key = "empty") {
        EmptyState()
      }
    } else {
      items(
        items = filteredOthers,
        key = { "stop_${it.busStopCode}" }
      ) { stop ->
        BusStopRow(
          stop = stop,
          isFavorite = true,
          onToggleFavorite = onToggleFavorite,
          modifier = Modifier.animateItem(),
        )
      }
    }
  }
}