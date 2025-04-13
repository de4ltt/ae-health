package com.ae.network.di.module

import com.ae.network.di.scope.CoreNetworkScope
import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory

@Module
object GsonConverterFactoryModule {

    @CoreNetworkScope
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

}