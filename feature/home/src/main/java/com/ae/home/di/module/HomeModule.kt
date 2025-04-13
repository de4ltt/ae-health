package com.ae.home.di.module

import com.ae.annotations.DefaultDispatcher
import com.ae.home.di.scope.HomeScope
import com.ae.home.viewmodel.SearchViewModelFactory
import com.ae.search.use_case.ISearchServiceTypesUseCase
import com.ae.search.use_case.ISearchWithFiltersUseCase
import com.ae.search.use_case.ISearchWithinRadiusUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher

@Module
internal class HomeModule {

    @HomeScope
    @Provides
    fun provideSearchViewModelFactory(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
        searchWithFiltersUseCase: ISearchWithFiltersUseCase,
        searchWithinRadiusUseCase: ISearchWithinRadiusUseCase,
        searchServiceTypeUseCase: ISearchServiceTypesUseCase
    ): SearchViewModelFactory = SearchViewModelFactory(
        defaultDispatcher,
        searchWithFiltersUseCase,
        searchWithinRadiusUseCase,
        searchServiceTypeUseCase
    )

}