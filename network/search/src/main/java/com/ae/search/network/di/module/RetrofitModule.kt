package com.ae.search.network.di.module

import com.ae.network.model.interfaces.ISecretProperties
import com.ae.network.retrofit.IMapSearchApi
import com.ae.search.network.di.scope.NetworkScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
internal class RetrofitModule {

    @NetworkScope
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        secretProperties: ISecretProperties,
        converterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(secretProperties.defaultUri)
            .addConverterFactory(converterFactory)
            .client(client)
            .build()

    @NetworkScope
    @Provides
    fun provideSearchApi(retrofit: Retrofit): IMapSearchApi =
        retrofit.create(IMapSearchApi::class.java)

}