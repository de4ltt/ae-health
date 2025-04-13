package com.ae.network.di.module

import com.ae.network.di.scope.CoreNetworkScope
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor

@Module
object HttpInterceptorModule {

    @CoreNetworkScope
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor { message ->
        println("HTTP: $message")
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

}