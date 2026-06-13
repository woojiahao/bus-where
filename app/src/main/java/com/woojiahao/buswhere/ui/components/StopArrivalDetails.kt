package com.woojiahao.buswhere.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.woojiahao.buswhere.repository.BusWhereArrivalState
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun StopArrivalDetails(
  arrivalState: BusWhereArrivalState,
  onRetry: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .padding(horizontal = 16.dp, vertical = 10.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    when (arrivalState) {
      BusWhereArrivalState.Error -> {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "Couldn't load arrivals",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          TextButton(onClick = onRetry) { Text("Retry") }
        }
      }

      BusWhereArrivalState.Idle -> Unit
      BusWhereArrivalState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
      is BusWhereArrivalState.Success -> {
        if (arrivalState.arrivals.all { !it.hasBuses }) {
          Text("No bus information available")
        } else {
          Box(modifier = Modifier.fillMaxWidth().padding(0.dp), contentAlignment = Alignment.CenterEnd) {
            IconButton(onClick = onRetry, modifier = Modifier.size(24.dp)) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
              )
            }
          }
          arrivalState.arrivals
            .filter { it.hasBuses }
            .forEach { BusArrivalDetail(it) }
        }
      }
    }
  }
}