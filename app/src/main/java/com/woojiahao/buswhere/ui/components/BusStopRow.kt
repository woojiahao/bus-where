package com.woojiahao.buswhere.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.woojiahao.buswhere.models.Stop
import com.woojiahao.buswhere.repository.BusWhereArrivalState

@Composable
fun BusStopRow(
  stop: Stop,
  isExpanded: Boolean,
  isFavorite: Boolean,
  showDivider: Boolean,
  arrivalState: BusWhereArrivalState,
  onToggleFavorite: (stop: Stop) -> Unit,
  onToggleExpand: () -> Unit,
  onFetchArrivals: (busStopCode: Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val starTint by animateColorAsState(
    targetValue = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
    label = "star_tint"
  )

  LaunchedEffect(isExpanded) {
    if (isExpanded) {
      onFetchArrivals(stop.busStopCode)
    }
  }

  ListItem(
    modifier = modifier.clickable { onToggleExpand() },
    headlineContent = { Text(stop.description) },
    supportingContent = { Text("${stop.busStopCode} | ${stop.roadName}") },
    leadingContent = {
      Icon(
        imageVector = Icons.Default.DirectionsBus,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant
      )
    },
    trailingContent = {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
          contentDescription = if (isExpanded) "Collapse" else "Expand",
          tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        IconButton(onClick = { onToggleFavorite(stop) }) {
          Icon(
            imageVector = if (isFavorite) Icons.Default.Star else Icons.Outlined.Star,
            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
            tint = starTint,
          )
        }
      }
    }
  )

  AnimatedVisibility(
    visible = isExpanded,
    enter = expandVertically() + fadeIn(),
    exit = shrinkVertically() + fadeOut()
  ) {
    StopArrivalDetails(
      arrivalState = arrivalState,
      onRetry = { onFetchArrivals(stop.busStopCode) },
    )
  }

  if (showDivider) {
    HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
  }
}