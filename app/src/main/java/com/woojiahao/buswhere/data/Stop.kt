package com.woojiahao.buswhere.data

data class Stop(
    val description: String,
    val latitude: Float,
    val longitude: Float,
    val busStopCode: Int,
    val roadName: String,
)