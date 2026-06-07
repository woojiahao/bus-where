package com.woojiahao.buswhere.data.api.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class BusWhereApiRouteDto(
  @SerialName(value = "service_no")
  val serviceNo: String,
  @SerialName(value = "bus_stop_code")
  val busStopCode: Int,
  val direction: Int,
  @SerialName(value = "stop_sequence")
  val stopSequence: Int
)