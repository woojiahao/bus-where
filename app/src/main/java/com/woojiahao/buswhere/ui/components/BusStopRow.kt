package com.woojiahao.buswhere.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.woojiahao.buswhere.models.Stop

@Composable
fun BusStopRow(
  stop: Stop,
  isFavorite: Boolean,
  onToggleFavorite: (stop: Stop) -> Unit,
  modifier: Modifier = Modifier
) {
  val starTint by animateColorAsState(
    targetValue = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
    label = "star_tint"
  )

  ListItem(
    modifier = modifier,
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
      IconButton(onClick = { onToggleFavorite(stop) }) {
        Icon(
          imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
          contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
          tint = starTint,
        )
      }
    }
  )
  HorizontalDivider(modifier = modifier.padding(horizontal = 16.dp))
}