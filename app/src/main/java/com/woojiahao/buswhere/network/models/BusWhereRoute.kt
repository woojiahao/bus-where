package com.woojiahao.buswhere.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class BusWhereRoute(
    @SerialName(value="service_no")
    val serviceNo: String,
    @SerialName(value="bus_stop_code")
    val busStopCode: Int,
    val direction: Int,
    @SerialName(value = "stop_sequence")
    val stopSequence: Int
)