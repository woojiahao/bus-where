package com.woojiahao.buswhere.data

data class Service(
    val key: Key,
    val stops: List<Stop> = emptyList(),
) {
    data class Key(
        val serviceNo: String,
        val direction: Int
    )
}