package com.woojiahao.buswhere.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.woojiahao.buswhere.models.Service
import com.woojiahao.buswhere.models.Stop
import com.woojiahao.buswhere.ui.theme.SecondaryContainer

@Composable
fun BusStopWidgetSelectionRow(
  stop: Stop,
  stopServices: List<Service>,
  isExpanded: Boolean,
  isFavorite: Boolean,
  onToggleExpand: () -> Unit,
  onSelectService: (service: Service) -> Unit,
  modifier: Modifier = Modifier
) {
  val starTint by animateColorAsState(
    targetValue = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
    label = "star_tint"
  )

  Box(
    modifier = modifier
      .padding(16.dp, 2.dp)
      .clip(RoundedCornerShape(15.dp))
  ) {
    ListItem(
      modifier = modifier
        .clickable { onToggleExpand() },
      headlineContent = { Text(stop.description) },
      supportingContent = { Text("${stop.busStopCode} | ${stop.roadName}") },
      colors = ListItemDefaults.colors(SecondaryContainer),
      trailingContent = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
          Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
          )

          Icon(
            imageVector = if (isFavorite) Icons.Default.Star else Icons.Outlined.Star,
            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
            tint = starTint
          )
        }
      }
    )
  }

  AnimatedVisibility(
    visible = isExpanded,
    enter = expandVertically() + fadeIn(),
    exit = shrinkVertically() + fadeOut()
  ) {
    Column(
      modifier =
        Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
      stopServices.forEach {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
        ) {
          Text(
            modifier = Modifier
              .weight(1f)
              .align(Alignment.CenterVertically),
            text = it.key.serviceNo
          )
          Button(
            onClick = { onSelectService(it) }, modifier = Modifier
              .weight(1f)
              .align(Alignment.CenterVertically)
          ) {
            Text(text = "Select stop")
          }
        }
      }
    }
  }
}