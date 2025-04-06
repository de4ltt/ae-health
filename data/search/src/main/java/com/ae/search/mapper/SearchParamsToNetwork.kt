package com.ae.search.mapper

import com.ae.network.model.SearchItemCategory
import com.ae.network.model.SearchParamsNetwork
import com.ae.search.model.SearchParams

internal fun SearchParams.toNetwork(): SearchParamsNetwork =
    SearchParamsNetwork(
        query = query,
        itemFilters = itemsFilters as List<SearchItemCategory>,
        radius = radius,
        lat = lat,
        lon = lon
    )