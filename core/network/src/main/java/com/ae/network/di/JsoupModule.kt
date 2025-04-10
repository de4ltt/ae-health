package com.ae.network.di

import com.ae.network.jsoup.IJsoupFindApi
import com.ae.network.jsoup.IJsoupMapApi
import com.ae.network.jsoup.implementation.JsoupFindApi
import com.ae.network.jsoup.implementation.JsoupMapApi
import dagger.Binds
import dagger.Module

@Module
internal abstract class JsoupModule {

    @NetworkScope
    @Binds
    abstract fun bindJsoupMapApi(jsoupMapApi: JsoupMapApi): IJsoupMapApi

    @NetworkScope
    @Binds
    abstract fun bindJsoupFindApi(jsoupFindApi: JsoupFindApi): IJsoupFindApi

}