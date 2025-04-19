package com.ae.search.di.module

import com.ae.search.di.annotation.Doctor
import com.ae.search.di.annotation.Lpu
import com.ae.search.di.annotation.Service
import com.ae.search.di.scope.NetworkScope
import com.ae.search.jsoup.IJsoupNearbySearchApi
import com.ae.search.jsoup.IJsoupFindApi
import com.ae.search.jsoup.implementation.DoctorJsoupFindApi
import com.ae.search.jsoup.implementation.DoctorJsoupNearbySearchApi
import com.ae.search.jsoup.implementation.LpuJsoupFindApi
import com.ae.search.jsoup.implementation.LpuJsoupNearbySearchApi
import com.ae.search.jsoup.implementation.ServiceJsoupFindApi
import com.ae.search.jsoup.implementation.ServiceJsoupNearbySearchApi
import dagger.Binds
import dagger.Module

@Module
internal abstract class JsoupFindModule {

    @NetworkScope
    @Binds
    @Doctor
    abstract fun bindDoctorJsoupFindApi(jsoupFindApi: DoctorJsoupFindApi): IJsoupFindApi

    @NetworkScope
    @Binds
    @Lpu
    abstract fun bindLpuJsoupFindApi(jsoupFindApi: LpuJsoupFindApi): IJsoupFindApi

    @NetworkScope
    @Binds
    @Service
    abstract fun bindServiceJsoupFindApi(jsoupFindApi: ServiceJsoupFindApi): IJsoupFindApi

}