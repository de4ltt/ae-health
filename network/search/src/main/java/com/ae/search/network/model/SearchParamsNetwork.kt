package com.ae.search.network.model

import com.ae.search.network.model.enums.SearchItemCategory

data class SearchParamsNetwork(
    val query: String,
    val itemFilters: List<SearchItemCategory>,
    val radius: Int?,
    val lat: Double,
    val lon: Double
)