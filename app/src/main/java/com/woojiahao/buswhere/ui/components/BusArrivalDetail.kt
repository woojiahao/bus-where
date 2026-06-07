package com.woojiahao.buswhere.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.woojiahao.buswhere.models.ARRIVAL_BUFFER_MINUTES
import com.woojiahao.buswhere.models.Arrival
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun BusArrivalDetail(
  arrival: Arrival,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(32.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, text = arrival.serviceNo)

    val timeNow = Clock.System.now().plus(8.hours)
    arrival.nextBuses
      .forEach { bus ->
        val timeToArrive = bus.estimatedArrival!!.minus(timeNow).inWholeMinutes
        if (timeToArrive >= ARRIVAL_BUFFER_MINUTES) {
          val fillColor = when (bus.load!!) {
            "seats_available" -> MaterialTheme.colorScheme.primary
            "standing_available" -> MaterialTheme.colorScheme.tertiary
            "limited_standing" -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.onSurfaceVariant
          }
          Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(if (timeToArrive <= 0L) "Arr" else timeToArrive.toString())
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .drawBehind { drawRect(color = fillColor) }
            )
          }
        }
      }
    repeat((0..(3 - arrival.nextBuses.size)).count()) {
      Column(modifier = Modifier.weight(1f), Arrangement.spacedBy(4.dp)) {}
    }
  }
}