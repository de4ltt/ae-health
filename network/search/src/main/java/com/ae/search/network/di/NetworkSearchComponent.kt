package com.ae.search.network.di

import com.ae.di.DispatchersComponent
import com.ae.network.ISearchDataSource
import com.ae.network.di.module.DataSourceModule
import com.ae.network.di.module.JsoupModule
import com.ae.network.di.module.RetrofitModule
import com.ae.network.di.scope.NetworkScope
import dagger.Component

@NetworkScope
@Component(
    modules = [DataSourceModule::class, JsoupModule::class, RetrofitModule::class],
    dependencies = [DispatchersComponent::class]
)
interface NetworkSearchComponent {

    fun searchDataSource(): ISearchDataSource

    @Component.Builder
    interface Builder {

        fun bindDispatchersComponent(dispatchersComponent: DispatchersComponent): Builder

        fun build(): NetworkSearchComponent
    }
}