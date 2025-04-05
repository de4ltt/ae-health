package com.ae.search.mapper

import com.ae.network.model.SearchParamsNetwork
import com.ae.search.model.SearchParams

fun SearchParams.toNetwork(): SearchParamsNetwork =
    SearchParamsNetwork(
        query = query,
        itemsFilters = itemsFilters.map { it.toString() },
        radius = radius,
        lat = lat,
        lon = lon
    )