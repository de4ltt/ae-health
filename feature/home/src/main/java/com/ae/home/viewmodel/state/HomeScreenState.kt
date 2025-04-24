package com.ae.home.viewmodel.state

import com.ae.network.model.NetworkRequestError
import com.ae.search.model.interfaces.ISearchItem
import kotlinx.coroutines.flow.StateFlow

sealed interface HomeScreenState {
    data object Default: HomeScreenState
    data object Filters: HomeScreenState
    data class Items(val itemsFlow: StateFlow<List<ISearchItem>>) : HomeScreenState
    data object Loading : HomeScreenState
}