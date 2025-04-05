package com.ae.network.model

data class SearchParamsNetwork(
    val query: String,
    val itemsFilters: List<String>,
    val radius: Int?,
    val lat: Double,
    val lon: Double
)