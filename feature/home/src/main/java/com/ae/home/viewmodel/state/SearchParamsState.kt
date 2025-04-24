package com.ae.home.viewmodel.state

import com.ae.home.model.SearchItemCategoryUI
import com.ae.home.model.SearchRadius
import com.ae.home.model.SortBy
import com.ae.home.model.UserLocation
import com.ae.search.model.item.util.SearchItemCategory
import com.ae.search.model.search.SearchParams

data class SearchParamsState(
    val query: String? = null,
    val categories: List<SearchItemCategoryUI> = listOf(SearchItemCategoryUI.Lpu),
    val sort: SortBy = SortBy.NAME,
    val radius: SearchRadius = SearchRadius.UNSET,
    val location: UserLocation? = null,
) {

    fun toSearchParams(location: UserLocation): SearchParams = SearchParams(
        query = query ?: "",
        itemsFilters = categories.mapNotNull { SearchItemCategory.fromString(it.name) },
        radius = radius.radius,
        lat = location.lat,
        lon = location.lon
    )

}