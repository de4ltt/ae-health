package com.ae.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ae.annotations.DefaultDispatcher
import com.ae.network.request_result.NetworkRequestError
import com.ae.search.model.ISearchItem
import com.ae.search.model.SearchItemCategory
import com.ae.search.model.SearchParams
import com.ae.search.use_case.ISearchWithFiltersUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val searchWithFiltersUseCase: ISearchWithFiltersUseCase
) : ViewModel() {

    private val _foundObjects: MutableStateFlow<List<ISearchItem>> = MutableStateFlow(emptyList())
    val foundObjects = _foundObjects.asStateFlow()

    private val _exception: MutableStateFlow<String?> = MutableStateFlow(null)
    val exception = _exception.asStateFlow()

    fun onRegularSearch() = viewModelScope.launch(defaultDispatcher) {

        _exception.value = "Loading"

        try {
            val results = searchWithFiltersUseCase.invoke(
                SearchParams(
                    "Гор", listOf(SearchItemCategory.LPU), null, 0.0, 0.0
                )
            )

            _foundObjects.value = results
            _exception.value = null
        } catch (e: NetworkRequestError) {
            _exception.value = e.message
        }
    }

    fun onNearbySearch() = viewModelScope.launch(defaultDispatcher) {

        _exception.value = "Loading"

        try {
            val results = searchWithFiltersUseCase.invoke(
                SearchParams(
                    "Гор", listOf(SearchItemCategory.SERVICES), 100, 45.018952, 39.030092
                )
            )

            _foundObjects.value = results
            _exception.value = null
        } catch (e: NetworkRequestError) {
            _exception.value = e.message
        }
    }
}