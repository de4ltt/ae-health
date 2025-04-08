package com.ae.network.di

import com.ae.annotations.DefaultDispatcher
import com.ae.annotations.IoDispatcher
import com.ae.di.DispatchersComponent
import com.ae.network.ISearchDataSource
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Component(modules = [DataSourceModule::class, JsoupModule::class, RetrofitModule::class, SecretPropertiesModule::class], dependencies = [DispatchersComponent::class])
interface NetworkComponent {

    fun searchDataSource(): ISearchDataSource

    @Component.Builder
    interface Builder {

        fun bindDispatchersComponent(dispatchersComponent: DispatchersComponent): Builder

        fun build(): NetworkComponent
    }
}