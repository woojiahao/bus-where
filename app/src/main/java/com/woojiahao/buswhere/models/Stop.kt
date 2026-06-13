package com.woojiahao.buswhere.models

data class Stop(
  val description: String,
  val latitude: Float,
  val longitude: Float,
  val busStopCode: Int,
  val roadName: String,
)

infix fun List<Stop>.search(q: String): List<Stop> {
  return this.filter {
    q.isEmpty()
        || it.description.lowercase().contains(q)
        || it.roadName.lowercase().contains(q)
        || it.busStopCode.toString().contains(q)
  }
}