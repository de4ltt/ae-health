package com.ae.network.di

import com.ae.network.model.ISecretProperties
import com.ae.network.retrofit.ISearchApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
internal class RetrofitModule {

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    //    @Singleton
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

    //    @Singleton
    @Provides
    fun provideSearchApi(retrofit: Retrofit): ISearchApi =
        retrofit.create(ISearchApi::class.java)

}