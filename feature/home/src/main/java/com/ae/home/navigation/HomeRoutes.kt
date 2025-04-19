package com.ae.home.navigation

sealed class HomeRoutes(val route: String) {
    operator fun invoke() = route

    data object Search: HomeRoutes("search")
}