package com.ae.network.di

import com.ae.network.ISearchDataSource
import com.ae.network.data_source.SearchDataSource
import com.ae.network.jsoup.IJsoupApi
import com.ae.network.jsoup.JsoupApi
import com.ae.network.model.ISecretProperties
import com.ae.network.model.SecretProperties
import com.ae.network.retrofit.ISearchApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Singleton
@Module
internal abstract class NetworkModule {

    @Singleton
    @Binds
    abstract fun bindSearchDataSource(searchDataSource: SearchDataSource): ISearchDataSource

    @Singleton
    @Binds
    abstract fun bindJsoupApi(jsoupApi: JsoupApi): IJsoupApi

    @Singleton
    @Binds
    abstract fun bindSecretProperties(secretProperties: SecretProperties): ISecretProperties

    @Provides
    @Singleton
    fun provideRetrofit(secretProperties: ISecretProperties): Retrofit =
        Retrofit.Builder()
            .baseUrl(secretProperties.defaultUri)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideSearchApi(retrofit: Retrofit): ISearchApi =
        retrofit.create(ISearchApi::class.java)

}