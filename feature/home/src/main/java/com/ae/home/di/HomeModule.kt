package com.ae.home.di

import android.icu.text.DisplayOptions
import com.ae.annotations.DefaultDispatcher
import com.ae.home.viewmodel.SearchViewModel
import com.ae.home.viewmodel.SearchViewModelFactory
import com.ae.search.use_case.ISearchServiceTypesUseCase
import com.ae.search.use_case.ISearchWithFiltersUseCase
import com.ae.search.use_case.ISearchWithinRadiusUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

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