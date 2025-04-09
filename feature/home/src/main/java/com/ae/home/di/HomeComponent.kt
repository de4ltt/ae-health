package com.ae.home.di

import com.ae.di.DispatchersComponent
import com.ae.home.viewmodel.SearchViewModelFactory
import com.ae.search.di.SearchDataComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@HomeScope
@Component(
    modules = [HomeModule::class],
    dependencies = [DispatchersComponent::class, SearchDataComponent::class]
)
interface HomeComponent {

    fun searchViewModelFactory(): SearchViewModelFactory

    @Component.Builder
    interface Builder {

        fun bindDispatchersComponent(dispatchersComponent: DispatchersComponent): Builder

        fun bindSearchDataComponent(searchDataComponent: SearchDataComponent): Builder

        fun build(): HomeComponent
    }
}