package com.ae.search.mapper

import com.ae.search.model.SearchItemCategory as CategoryNetwork
import com.ae.search.model.item.util.SearchItemCategory as CategoryDomain
import com.ae.search.model.search.SearchParams
import com.ae.search.model.SearchParamsNetwork

internal fun List<CategoryDomain>.toNetwork(): List<CategoryNetwork> =
    this.mapNotNull { CategoryNetwork.fromString(it.toString()) }

internal fun SearchParams.toNetwork(): SearchParamsNetwork =
    SearchParamsNetwork(
        query = query,
        itemFilters = itemsFilters.toNetwork(),
        radius = radius,
        lat = lat,
        lon = lon
    )