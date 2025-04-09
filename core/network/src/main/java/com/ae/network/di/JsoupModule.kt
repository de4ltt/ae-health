package com.ae.network.di

import com.ae.network.jsoup.IJsoupMapApi
import com.ae.network.jsoup.implementation.JsoupMapApi
import dagger.Binds
import dagger.Module

@Module
internal abstract class JsoupModule {

    @NetworkScope
    @Binds
    abstract fun bindJsoupApi(jsoupApi: JsoupMapApi): IJsoupMapApi

}