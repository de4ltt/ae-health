package com.ae.mylibrary.resources

import com.ae.ui.R

sealed interface AppStrings {

    object Errors {
        val categoriesEmpty = R.string.categories_empty
    }

    object Common {
        val doctor = R.string.doctor
        val clinic = R.string.clinic
        val services = R.string.services
    }

    object HomeStrings {
        val searchBarHint = R.string.search_bar_hint

        val name = R.string.name
        val rating = R.string.rating
        val experience = R.string.experience
        val distance = R.string.distance

        val unset = R.string.unset
        val m500 = R.string.m500
        val km1 = R.string.km1
        val km2 = R.string.km2
    }

}