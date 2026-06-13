package com.woojiahao.buswhere.data.api.dtos

import com.woojiahao.buswhere.models.Service
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class BusWhereApiServiceDto(
  val category: String,
  val operator: String,
  val direction: Int,
  @SerialName(value = "origin_code")
  val originCode: Int,
  @SerialName(value = "destination_code")
  val destinationCode: Int,
  @SerialName(value = "service_no")
  val serviceNo: String
) {
  fun toModel(): Service {
    return Service(
      key = Service.Key(serviceNo, direction),
      stops = emptyList()
    )
  }
}