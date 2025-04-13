package com.ae.search.di

import com.ae.search.use_case.ISearchServiceTypesUseCase
import com.ae.search.use_case.ISearchWithFiltersUseCase
import com.ae.search.use_case.ISearchWithinRadiusUseCase
import com.ae.search.use_case.SearchServiceTypesUseCase
import com.ae.search.use_case.SearchWithFiltersUseCase
import com.ae.search.use_case.SearchWithinRadiusUseCase
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal abstract class SearchUseCaseModule {

    @SearchDataScope
    @Binds
    abstract fun bindSearchWithFiltersUseCase(searchWithFiltersUseCase: SearchWithFiltersUseCase): ISearchWithFiltersUseCase

    @SearchDataScope
    @Binds
    abstract fun bindSearchWithinRadiusUseCase(searchWithinRadiusUseCase: SearchWithinRadiusUseCase): ISearchWithinRadiusUseCase

    @SearchDataScope
    @Binds
    abstract fun bindSearchServiceTypesUseCase(searchServiceTypesUseCase: SearchServiceTypesUseCase): ISearchServiceTypesUseCase

}