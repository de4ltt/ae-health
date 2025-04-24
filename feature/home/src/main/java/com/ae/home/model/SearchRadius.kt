package com.ae.home.model

import androidx.annotation.StringRes
import com.ae.mylibrary.resources.AppStrings

enum class SearchRadius(
    @StringRes val title: Int,
    val radius: Int? = null
) {
    UNSET(title = AppStrings.HomeStrings.unset),
    M500(title = AppStrings.HomeStrings.m500, radius = 500),
    KM1(title = AppStrings.HomeStrings.km1, radius = 1000),
    KM2(title = AppStrings.HomeStrings.km2, radius = 2000)
}