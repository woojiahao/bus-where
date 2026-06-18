package com.woojiahao.buswhere.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.woojiahao.buswhere.ui.theme.Error
import com.woojiahao.buswhere.ui.theme.OnPrimaryContainer
import com.woojiahao.buswhere.ui.theme.Primary
import com.woojiahao.buswhere.ui.theme.Warning
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
    verticalAlignment = Alignment.CenterVertically
  ) {
    // 1. Service number takes up the remaining space on the left
    Text(
      modifier = Modifier.weight(1f),
      fontWeight = FontWeight.Bold,
      text = arrival.serviceNo,
      color = OnPrimaryContainer,
    )

    val timeNow = Clock.System.now().plus(8.hours)

    // Track how many slots we actually render
    var renderedSlots = 0

    arrival.nextBuses.forEach { bus ->
      val timeToArrive = bus.estimatedArrival!!.minus(timeNow).inWholeMinutes
      if (timeToArrive >= ARRIVAL_BUFFER_MINUTES) {
        renderedSlots++
        val fillColor = when (bus.load!!) {
          "seats_available" -> Primary
          "standing_available" -> Warning
          "limited_standing" -> Error
          else -> Primary
        }

        // 2. Each slot takes an equal weight block
        Column(
          modifier = Modifier.weight(1f),
          horizontalAlignment = Alignment.CenterHorizontally, // Centers the text and bar inside the 1/4 space
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            color = OnPrimaryContainer,
            text = if (timeToArrive <= 0L) "Arr" else timeToArrive.toString()
          )
          Box(
            modifier = Modifier
              .size(width = 45.dp, height = 8.dp) // Fixed width for the bar inside its 1/4 slot
              .clip(RoundedCornerShape(4.dp))
              .drawBehind { drawRect(color = fillColor) }
          )
        }
      }
    }

    // 3. Fill the remaining 3 slots with empty weights so the grid doesn't shift
    val maxSlots = 3
    repeat(maxSlots - renderedSlots) {
      Box(modifier = Modifier.weight(1f))
    }
  }
}