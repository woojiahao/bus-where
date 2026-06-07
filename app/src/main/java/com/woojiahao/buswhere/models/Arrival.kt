package com.woojiahao.buswhere.models

import kotlinx.datetime.Instant
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

// -2 is the arbitrary buffer just in case the bus might be late
const val ARRIVAL_BUFFER_MINUTES = -2

data class Arrival(
  val serviceNo: String,
  val nextBus1: Bus,
  val nextBus2: Bus,
  val nextBus3: Bus,
) {
  data class Bus @OptIn(ExperimentalTime::class) constructor(
    val load: String? = null,
    val estimatedArrival: Instant? = null
  )

  @OptIn(ExperimentalTime::class)
  val nextBuses: List<Bus>
    get() {
      val allBuses = listOf(nextBus1, nextBus2, nextBus3)
      val timeNow = Clock.System.now().plus(8.hours)
      return allBuses.filter {
        it.estimatedArrival != null
            && it.estimatedArrival.minus(timeNow).inWholeMinutes >= ARRIVAL_BUFFER_MINUTES
      }
    }

  val hasBuses: Boolean
    get() = nextBuses.isNotEmpty()
}