package com.ae.search.model.search

import com.ae.search.model.item.util.SearchItemCategory

data class SearchParams(
    val query: String,
    val itemsFilters: List<SearchItemCategory>,
    val radius: Int?,
    val lat: Double,
    val lon: Double
)