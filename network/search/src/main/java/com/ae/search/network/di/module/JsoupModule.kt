package com.ae.search.network.di.module

import com.ae.search.network.di.scope.NetworkScope
import com.ae.search.network.jsoup.IJsoupFindApi
import com.ae.search.network.jsoup.implementation.JsoupFindApi
import dagger.Binds
import dagger.Module

@Module
internal abstract class JsoupModule {

    @NetworkScope
    @Binds
    abstract fun bindJsoupFindApi(jsoupFindApi: JsoupFindApi): IJsoupFindApi

}