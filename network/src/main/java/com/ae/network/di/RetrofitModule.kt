package com.ae.network.di

import com.ae.network.model.interfaces.ISecretProperties
import com.ae.network.retrofit.IMapSearchApi
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
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @NetworkScope
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor { message ->
        println("HTTP: $message")
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @NetworkScope
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

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