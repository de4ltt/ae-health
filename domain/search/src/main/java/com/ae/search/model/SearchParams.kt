package com.ae.search.model

data class SearchParams(
    val query: String,
    val itemsFilters: List<SearchItemCategory>,
    val radius: Int?,
    val lat: Double,
    val lon: Double
)