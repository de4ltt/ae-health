package com.ae.search.di

import com.ae.network.di.NetworkComponent
import com.ae.search.repository.ISearchRepository
import com.ae.search.use_case.ISearchWithFiltersUseCase
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@SearchDataScope
@Component(modules = [SearchModule::class, SearchUseCaseModule::class], dependencies = [NetworkComponent::class])
interface SearchDataComponent {

    fun searchRepository(): ISearchRepository

    fun searchWithFiltersUseCase(): ISearchWithFiltersUseCase

    @Component.Builder
    interface Builder {

        fun bindNetworkComponent(networkComponent: NetworkComponent): Builder

        fun build(): SearchDataComponent
    }
}