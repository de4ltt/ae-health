package com.ae.search.network.di.module

import com.ae.config.di.annotation.SecretProperty
import com.ae.search.network.di.scope.NetworkScope
import com.ae.search.network.retrofit.IMapSearchApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
internal class RetrofitModule {

    @NetworkScope
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        @SecretProperty("base_url") baseUrl: String,
        converterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()

    @NetworkScope
    @Provides
    fun provideSearchApi(retrofit: Retrofit): IMapSearchApi =
        retrofit.create(IMapSearchApi::class.java)

}