package com.ae.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.ae.home.screen.HomeScreen
import com.ae.home.viewmodel.SearchViewModel

fun NavGraphBuilder.homeNavGraph(
    navHostController: NavHostController,
    searchViewModel: SearchViewModel
) {
    composable(route = HomeRoutes.Search()) {
        HomeScreen(
            navHostController = navHostController,
            searchViewModel = searchViewModel
        )
    }
}