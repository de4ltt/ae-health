package com.ae.network.model

data class SearchParamsNetwork(
    val query: String,
    val itemFilters: List<SearchItemCategory>,
    val radius: Int?,
    val lat: Double,
    val lon: Double
)