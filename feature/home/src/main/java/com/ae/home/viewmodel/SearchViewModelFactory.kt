package com.ae.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ae.annotations.DefaultDispatcher
import com.ae.search.use_case.ISearchServiceTypesUseCase
import com.ae.search.use_case.ISearchWithFiltersUseCase
import com.ae.search.use_case.ISearchWithinRadiusUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

class SearchViewModelFactory @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val searchWithFiltersUseCase: ISearchWithFiltersUseCase,
    private val searchWithinRadiusUseCase: ISearchWithinRadiusUseCase,
    private val searchServiceTypeUseCase: ISearchServiceTypesUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(
                defaultDispatcher,
                searchWithFiltersUseCase,
                searchWithinRadiusUseCase,
                searchServiceTypeUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}