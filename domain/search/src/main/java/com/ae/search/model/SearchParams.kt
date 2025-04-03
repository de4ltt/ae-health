package com.ae.search.model

data class SearchParams(
    val query: String,
    val itemsFilters: List<SearchItemCategory>,
    val lat: Double,
    val lon: Double,
    val radius: Int
)
