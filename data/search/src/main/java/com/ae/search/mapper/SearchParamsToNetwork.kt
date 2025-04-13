package com.ae.search.mapper

import com.ae.search.model.item.util.SearchItemCategory as CategoriesDomain
import com.ae.network.model.SearchItemCategory as CategoriesNetwork
import com.ae.network.model.SearchParamsNetwork
import com.ae.search.model.search.SearchParams

internal fun List<CategoriesDomain>.toNetwork(): List<CategoriesNetwork> {

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