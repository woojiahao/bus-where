package com.woojiahao.buswhere.models

data class Bundle(
  val services: List<Service> = emptyList(),
  val stops: List<Stop> = emptyList()
) {
  fun isEmpty(): Boolean {
    return services.isEmpty() && stops.isEmpty()
  }
}