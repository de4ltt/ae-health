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

    @NetworkScope
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @NetworkScope
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()

    @NetworkScope
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Referer", "https://prodoctorov.ru/")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("Origin", "https://prodoctorov.ru")
                    .addHeader(
                        "Cookie",
                        "csrftoken=FOHwJ1BTmN8UeRuEPdEDZpIVfJeZFtfc; sessionid=3hynnh4afmazx81qp9ehmldo1e8kyvvg; _ym_uid=1743608668443699950; _ym_d=1743608668; _ym_isad=1; _ym_visorc=b"
                    )
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36")
                    .build()
                chain.proceed(request)
            }.addInterceptor(loggingInterceptor).build()

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
    fun provideSearchApi(retrofit: Retrofit): ISearchApi =
        retrofit.create(ISearchApi::class.java)

}