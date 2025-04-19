package com.ae.health

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ae.health.viewmodel_provider.AppViewModelProvider
import com.ae.home.navigation.HomeRoutes
import com.ae.home.navigation.homeNavGraph
import com.ae.home.viewmodel.SearchViewModel

@Composable
internal fun AppNavHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    viewModelProvider: AppViewModelProvider
) {

        NavHost(
            modifier = modifier,
            navController = navHostController,
            startDestination = HomeRoutes.Search()
        ) {
            homeNavGraph(
                navHostController = navHostController,
                searchViewModel = viewModelProvider<SearchViewModel>()
            )
        }

}
