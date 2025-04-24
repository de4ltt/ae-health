package com.ae.home.viewmodel.event

sealed interface HomeScreenEvent: HomeUIEvent {
    data object OnFiltersToggled : HomeScreenEvent
    data object OnSearchToggled: HomeScreenEvent
}