package com.ae.search.di.module

import com.ae.search.di.scope.NetworkScope
import com.ae.search.jsoup.IJsoupFindApi
import com.ae.search.jsoup.implementation.JsoupFindApi
import dagger.Binds
import dagger.Module

@Module
internal abstract class JsoupModule {

    @NetworkScope
    @Binds
    abstract fun bindJsoupFindApi(jsoupFindApi: JsoupFindApi): IJsoupFindApi

}