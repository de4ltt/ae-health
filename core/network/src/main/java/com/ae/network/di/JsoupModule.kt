package com.ae.network.di

import com.ae.network.jsoup.IJsoupApi
import com.ae.network.jsoup.JsoupApi
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal abstract class JsoupModule {

    @NetworkScope
    @Binds
    abstract fun bindJsoupApi(jsoupApi: JsoupApi): IJsoupApi

}