package com.ae.search.network.di

import com.ae.config.di.ConfigComponent
import com.ae.di.DispatchersComponent
import com.ae.network.di.CoreNetworkComponent
import com.ae.search.network.ISearchDataSource
import com.ae.search.network.di.module.DataSourceModule
import com.ae.search.network.di.module.JsoupModule
import com.ae.search.network.di.module.RetrofitModule
import com.ae.search.network.di.scope.NetworkScope
import dagger.Component

@NetworkScope
@Component(
    modules = [DataSourceModule::class, JsoupModule::class, RetrofitModule::class],
    dependencies = [DispatchersComponent::class, ConfigComponent::class, CoreNetworkComponent::class]
)
interface NetworkSearchComponent {

    fun searchDataSource(): ISearchDataSource

    @Component.Builder
    interface Builder {

        fun bindCoreNetworkComponent(coreNetworkComponent: CoreNetworkComponent): Builder

        fun bindConfigComponent(configComponent: ConfigComponent): Builder

        fun bindDispatchersComponent(dispatchersComponent: DispatchersComponent): Builder

        fun build(): NetworkSearchComponent
    }
}