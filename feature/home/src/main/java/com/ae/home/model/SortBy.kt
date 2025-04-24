package com.ae.home.model

import androidx.annotation.StringRes
import com.ae.mylibrary.resources.AppStrings
import com.ae.search.model.item.util.SearchItemCategory
import com.ae.search.model.item.util.SearchItemCategory.Lpu
import com.ae.search.model.item.util.SearchItemCategory.Services

enum class SortBy(
    @StringRes val title: Int,
    val disallowed: List<SearchItemCategory>? = null
) {
    NAME(title = AppStrings.HomeStrings.name),
    RATING(title = AppStrings.HomeStrings.rating, disallowed = listOf(Services)),
    DISTANCE(title = AppStrings.HomeStrings.distance),
    EXPERIENCE(title = AppStrings.HomeStrings.experience, disallowed = listOf(Services, Lpu))
}