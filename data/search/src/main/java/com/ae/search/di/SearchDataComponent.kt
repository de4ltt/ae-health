package com.ae.search.di

import com.ae.search.repository.ISearchRepository
import com.ae.search.use_case.ISearchServiceTypesUseCase
import com.ae.search.use_case.ISearchWithFiltersUseCase
import com.ae.search.use_case.ISearchWithinRadiusUseCase
import dagger.Component

@SearchDataScope
@Component(modules = [SearchModule::class, SearchUseCaseModule::class], dependencies = [NetworkSearchComponent::class])
interface SearchDataComponent {

    fun searchRepository(): ISearchRepository

    fun searchWithFiltersUseCase(): ISearchWithFiltersUseCase

    fun searchWithinRadiusUseCase(): ISearchWithinRadiusUseCase

    fun searchServiceTypesUseCase(): ISearchServiceTypesUseCase

    @Component.Builder
    interface Builder {

        fun bindNetworkSearchComponent(networkComponent: NetworkSearchComponent): Builder

        fun build(): SearchDataComponent
    }
}