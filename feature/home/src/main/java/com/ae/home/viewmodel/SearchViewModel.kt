package com.ae.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ae.annotations.DefaultDispatcher
import com.ae.network.request_result.NetworkRequestError
import com.ae.network_request.NetworkRequestError
import com.ae.network_request.NetworkRequestResult
import com.ae.search.model.ISearchItem
import com.ae.search.model.SearchItemCategory
import com.ae.search.model.SearchParams
import com.ae.search.use_case.ISearchServiceTypesUseCase
import com.ae.search.use_case.ISearchWithFiltersUseCase
import com.ae.search.use_case.ISearchWithinRadiusUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val searchWithFiltersUseCase: ISearchWithFiltersUseCase,
    private val searchWithinRadiusUseCase: ISearchWithinRadiusUseCase,
    private val searchServiceTypesUseCase: ISearchServiceTypesUseCase
) : ViewModel() {

    private val _foundObjects: MutableStateFlow<List<ISearchItem>> = MutableStateFlow(emptyList())
    val foundObjects = _foundObjects.asStateFlow()

    private val _exception: MutableStateFlow<String?> = MutableStateFlow(null)
    val exception = _exception.asStateFlow()

    fun onRegularSearch() = viewModelScope.launch {

        _exception.value = "Loading"

        try {
            val results = searchWithFiltersUseCase.invoke(
                SearchParams(
                    "Гор", SearchItemCategory.categoryValues, null, 0.0, 0.0
                )
            )

            _foundObjects.value = results
            _exception.value = if (results.isEmpty()) "Nothing was found" else null
        } catch (e: Throwable) {
            _exception.value = e.message
        }
    }

    fun onServiceTypeSearch(
        searchParams: SearchParams = SearchParams(
            query = "Гинеколог",
            itemsFilters = listOf(SearchItemCategory.Services),
            radius = 400,
            lat = 45.019061,
            lon = 39.030742
        )
    ) = viewModelScope.launch {

        _exception.value = "Loading"

        val results = searchServiceTypesUseCase.invoke(
            searchParams
        )

        if (results is NetworkRequestResult.Success) {

            val typeString = results[0].link!!.substring(
                results[0].link!!.subSequence(
                    0,
                    results[0].link!!.length - 1
                ).indexOfLast { it == '/' } + 1, results[0].link!!.length - 1
            )

            onNearbySearch(typeString, category = searchParams.itemsFilters[0])

            _exception.value = null
        } else _exception.value = (results as NetworkRequestResult.Error).error.message
    }

    fun onNearbySearch(type: String, category: SearchItemCategory) = viewModelScope.launch {

        _exception.value = "Loading"

        try {

            val results = searchWithinRadiusUseCase.invoke(
                SearchParams(
                    query = type, listOf(category),
                    radius = 400,
                    lat = 45.019061,
                    lon = 39.030742
                )
            )

            _foundObjects.value = results
            _exception.value = null
        } catch (e: Throwable) {
            _exception.value = e.message
        }
    }

}