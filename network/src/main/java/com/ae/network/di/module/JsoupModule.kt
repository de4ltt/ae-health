package com.ae.network.di.module

import com.ae.network.di.scope.NetworkScope
import com.ae.network.jsoup.IJsoupFindApi
import com.ae.network.jsoup.implementation.JsoupFindApi
import dagger.Binds
import dagger.Module

@Module
internal abstract class JsoupModule {

    @NetworkScope
    @Binds
    abstract fun bindJsoupFindApi(jsoupFindApi: JsoupFindApi): IJsoupFindApi

}