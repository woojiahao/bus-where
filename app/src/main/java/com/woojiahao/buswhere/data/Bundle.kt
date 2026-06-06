package com.woojiahao.buswhere.data

data class Bundle(
    val services: List<Service> = emptyList(),
    val stops: List<Stop> = emptyList()
) {
    fun isEmpty(): Boolean {
        return services.isEmpty() && stops.isEmpty()
    }
}