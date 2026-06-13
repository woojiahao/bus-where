package com.woojiahao.buswhere.data.api.dtos

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlin.time.ExperimentalTime

@Serializable
@JsonIgnoreUnknownKeys
data class BusWhereApiArrivalDto(
  @SerialName(value = "service_no")
  val serviceNo: String,
  @SerialName(value = "next_bus_1")
  val nextBus1: BusDto,
  @SerialName(value = "next_bus_2")
  val nextBus2: BusDto,
  @SerialName(value = "next_bus_3")
  val nextBus3: BusDto
) {
  @Serializable
  @JsonIgnoreUnknownKeys
  data class BusDto @OptIn(ExperimentalTime::class) constructor(
    val load: String? = null,
    @SerialName(value = "estimated_arrival")
    val estimatedArrival: Instant? = null
  )
}