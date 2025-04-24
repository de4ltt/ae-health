package com.ae.home.viewmodel.event

import com.ae.home.model.SearchItemCategoryUI
import com.ae.home.model.SearchRadius
import com.ae.home.model.SortBy
import com.ae.search.model.item.util.SearchItemCategory

sealed interface SearchParamsEvent: HomeUIEvent {
    data class OnValueChange(val newValue: String) : SearchParamsEvent
    data class OnCategoryToggled(val category: SearchItemCategoryUI) : SearchParamsEvent
    data class OnSortByToggled(val sort: SortBy) : SearchParamsEvent
    data class OnRadiusToggled(val radius: SearchRadius) : SearchParamsEvent
}