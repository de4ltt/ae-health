package com.ae.search.mapper

import com.ae.search.model.item.util.SearchItemCategory
import com.ae.search.model.search.SearchParams
import com.ae.search.network.model.SearchParamsNetwork
import com.ae.search.network.model.enums.SearchItemCategory as CategoriesNetwork

internal fun List<SearchItemCategory>.toNetwork(): List<CategoriesNetwork> {

    val entriesString = this.map { it.toString() }

    return CategoriesNetwork.entries.filter {
        entriesString.contains(it.toString())
    }
}

internal fun SearchParams.toNetwork(): SearchParamsNetwork =
    SearchParamsNetwork(
        query = query,
        itemFilters = itemsFilters.toNetwork(),
        radius = radius,
        lat = lat,
        lon = lon
    )