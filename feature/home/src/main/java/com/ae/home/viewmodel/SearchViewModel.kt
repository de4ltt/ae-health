package com.ae.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ae.annotations.DefaultDispatcher
import com.ae.home.model.SearchRadius
import com.ae.home.model.UserLocation
import com.ae.home.viewmodel.event.HomeScreenEvent
import com.ae.home.viewmodel.event.HomeUIEvent
import com.ae.home.viewmodel.event.SearchParamsEvent
import com.ae.home.viewmodel.state.HomeScreenState
import com.ae.home.viewmodel.state.HomeScreenState.Items
import com.ae.home.viewmodel.state.SearchParamsState
import com.ae.mylibrary.snackbar.SnackbarManager.showSnackbarMessage
import com.ae.mylibrary.snackbar.SnackbarMessage
import com.ae.network.model.NetworkRequestError
import com.ae.network.model.NetworkRequestResult
import com.ae.search.model.interfaces.ISearchItem
import com.ae.search.use_case.ISearchServiceTypesUseCase
import com.ae.search.use_case.ISearchWithFiltersUseCase
import com.ae.search.use_case.ISearchWithinRadiusUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val searchWithFiltersUseCase: ISearchWithFiltersUseCase,
    private val searchWithinRadiusUseCase: ISearchWithinRadiusUseCase,
    private val searchServiceTypesUseCase: ISearchServiceTypesUseCase,
) : ViewModel() {

    private val _searchParamsState = MutableStateFlow(SearchParamsState())
    val searchParamsState = _searchParamsState.asStateFlow()

    private val _homeScreenState = MutableStateFlow<HomeScreenState>(HomeScreenState.Default)
    val homeScreenState = _homeScreenState.asStateFlow()

    private val foundItems = MutableStateFlow<List<ISearchItem>>(emptyList())

    fun onEvent(event: HomeUIEvent) = when (event) {
        is HomeScreenEvent -> onHomeScreenEvent(event)
        is SearchParamsEvent -> onSearchParamsEvent(event)
    }

    private fun onHomeScreenEvent(event: HomeScreenEvent) = when (event) {
        HomeScreenEvent.OnFiltersToggled -> onFiltersToggled()
        HomeScreenEvent.OnSearchToggled -> onSearchToggled()
    }

    private fun onFiltersToggled() =
        viewModelScope.launch(handler) {
            when (_homeScreenState.value) {
                is HomeScreenState.Default, is HomeScreenState.Items ->
                    _homeScreenState.value = HomeScreenState.Filters

                is HomeScreenState.Filters ->
                    _homeScreenState.value =
                        if (foundItems.value.isEmpty()) HomeScreenState.Default
                        else Items(foundItems.asStateFlow())

                else -> {}
            }
        }

    private fun onSearchToggled() =
        viewModelScope.launch(handler) {

            val searchParamsState = _searchParamsState.value
            val location = UserLocation(lat = 45.033157, lon = 38.973768)
            val searchParams = searchParamsState.toSearchParams(location)

            _homeScreenState.value = HomeScreenState.Loading

            if (searchParamsState.radius != SearchRadius.UNSET)
                handleSearchResult(searchWithinRadiusUseCase(searchParams))
            else
                handleSearchResult(
                    if (searchParamsState.categories.size > 1) {
                        searchWithFiltersUseCase(searchParams)
                    } else {
                        searchServiceTypesUseCase(searchParams)
                    }
                )

        }

    private fun onSearchParamsEvent(event: SearchParamsEvent) = when (event) {
        is SearchParamsEvent.OnCategoryToggled -> onCategoryToggledHandler(event)
        is SearchParamsEvent.OnRadiusToggled -> onRadiusToggledHandler(event)
        is SearchParamsEvent.OnSortByToggled -> onSortByToggledHandler(event)
        is SearchParamsEvent.OnValueChange -> onValueChangeHandler(event)
    }

    private fun onValueChangeHandler(event: SearchParamsEvent.OnValueChange) =
        viewModelScope.launch(handler) {
            _searchParamsState.value = _searchParamsState.value.copy(
                query = if (event.newValue.isEmpty()) null else event.newValue
            )
        }

    private fun onCategoryToggledHandler(event: SearchParamsEvent.OnCategoryToggled) =
        viewModelScope.launch(handler) {

            val categories = _searchParamsState.value.categories.toMutableList()

            if (categories.contains(event.category)) {
                assert(categories.size - 1 != 0, lazyMessage = {

                })
                categories.remove(event.category)
            } else categories.add(event.category)

            _searchParamsState.value = _searchParamsState.value.copy(
                categories = categories
            )
        }

    private fun onRadiusToggledHandler(event: SearchParamsEvent.OnRadiusToggled) =
        viewModelScope.launch(handler) {
            _searchParamsState.value = _searchParamsState.value.copy(
                radius = event.radius
            )
        }

    private fun onSortByToggledHandler(event: SearchParamsEvent.OnSortByToggled) =
        viewModelScope.launch(handler) {
            _searchParamsState.value = _searchParamsState.value.copy(
                sort = event.sort
            )
        }

    private val handler = CoroutineExceptionHandler { _, exception ->

        if (exception is NetworkRequestError)
            showSnackbar(
                SnackbarMessage.ResultMessage.ErrorMessage(
                    message = exception.message ?: ""
                )
            )
        else showSnackbar(
            SnackbarMessage.ResultMessage.SuccessMessage(
                message = exception.message ?: ""
            )
        )
    }

    private fun showSnackbar(message: SnackbarMessage) =
        viewModelScope.showSnackbarMessage(message)

    private fun handleSearchResult(result: NetworkRequestResult<List<ISearchItem>>) {
        if (result is NetworkRequestResult.Success) {
            foundItems.value = result.data
            _homeScreenState.value = Items(foundItems)
        } else throw (result as NetworkRequestResult.Error).error
    }
}