package com.ae.home.di

import android.content.Context
import com.ae.di.DispatchersComponent
import com.ae.home.di.module.HomeModule
import com.ae.home.di.scope.HomeScope
import com.ae.home.viewmodel.SearchViewModelFactory
import com.ae.search.di.SearchDataComponent
import dagger.Component

@HomeScope
@Component(
    modules = [HomeModule::class],
    dependencies = [DispatchersComponent::class, SearchDataComponent::class]
)
interface HomeComponent {

    fun searchViewModelFactory(): SearchViewModelFactory

    @Component.Builder
    interface Builder {

        fun bindUtilComponent(dispatchersComponent: DispatchersComponent): Builder

        fun bindSearchDataComponent(searchDataComponent: SearchDataComponent): Builder

        fun build(): HomeComponent
    }
}