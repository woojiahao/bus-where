package com.woojiahao.buswhere.data.api.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class BusWhereApiStopDto(
  val description: String,
  val latitude: Float,
  val longitude: Float,
  @SerialName(value = "bus_stop_code")
  val busStopCode: Int,
  @SerialName(value = "road_name")
  val roadName: String
)