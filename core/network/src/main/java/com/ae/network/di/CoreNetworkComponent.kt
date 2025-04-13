package com.ae.network.di

import com.ae.config.di.ConfigComponent
import com.ae.network.di.module.GsonConverterFactoryModule
import com.ae.network.di.module.HttpInterceptorModule
import com.ae.network.di.module.OkHttpClientModule
import com.ae.network.di.scope.CoreNetworkScope
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

@CoreNetworkScope
@Component(
    modules = [GsonConverterFactoryModule::class, HttpInterceptorModule::class, OkHttpClientModule::class],
    dependencies = [ConfigComponent::class]
)
interface CoreNetworkComponent {

    fun okHttpClient(): OkHttpClient

    fun gsonConverterFactory(): GsonConverterFactory

    @Component.Builder
    interface Builder {

        fun bindConfigComponent(configComponent: ConfigComponent): Builder

        fun build(): CoreNetworkComponent
    }

}