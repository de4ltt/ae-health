package com.ae.search.model

import com.ae.search.model.SearchItemCategory

data class SearchParamsNetwork(
    val query: String,
    val itemFilters: List<SearchItemCategory>,
    val radius: Int?,
    val lat: Double,
    val lon: Double
)