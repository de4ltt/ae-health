package com.ae.search.di

import com.ae.search.use_case.ISearchWithFiltersUseCase
import com.ae.search.use_case.SearchWithFiltersUseCase
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal abstract class SearchUseCaseModule {

    @Binds
    abstract fun bindSearchWithFiltersUseCase(searchWithFiltersUseCase: SearchWithFiltersUseCase): ISearchWithFiltersUseCase

}