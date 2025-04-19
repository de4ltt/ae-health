package com.ae.search.di

import com.ae.config.di.ConfigComponent
import com.ae.di.DispatchersComponent
import com.ae.network.di.CoreNetworkComponent
import com.ae.search.ISearchDataSource
import com.ae.search.di.module.DataSourceModule
import com.ae.search.di.module.JsoupFindModule
import com.ae.search.di.module.JsoupNearbySearchModule
import com.ae.search.di.module.RetrofitModule
import com.ae.search.di.module.SearchItemCategoryModule
import com.ae.search.di.scope.NetworkScope
import com.ae.search.model.SearchItemCategory
import dagger.Component

@NetworkScope
@Component(
    modules = [DataSourceModule::class, JsoupNearbySearchModule::class, JsoupFindModule::class, RetrofitModule::class, SearchItemCategoryModule::class],
    dependencies = [DispatchersComponent::class, ConfigComponent::class, CoreNetworkComponent::class]
)
interface NetworkSearchComponent {

    fun searchDataSource(): ISearchDataSource

    fun searchItemCategories(): List<SearchItemCategory>

    @Component.Builder
    interface Builder {

        fun bindCoreNetworkComponent(coreNetworkComponent: CoreNetworkComponent): Builder

        fun bindConfigComponent(configComponent: ConfigComponent): Builder

        fun bindDispatchersComponent(dispatchersComponent: DispatchersComponent): Builder

        fun build(): NetworkSearchComponent
    }
}