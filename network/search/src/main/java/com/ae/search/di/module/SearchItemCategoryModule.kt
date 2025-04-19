package com.ae.search.di.module

import com.ae.search.di.annotation.Doctor
import com.ae.search.di.annotation.Lpu
import com.ae.search.di.annotation.Service
import com.ae.search.di.scope.NetworkScope
import com.ae.search.jsoup.IJsoupFindApi
import com.ae.search.jsoup.IJsoupNearbySearchApi
import com.ae.search.model.SearchItemCategory
import dagger.Module
import dagger.Provides

@Module
internal object SearchItemCategoryModule {

    @Provides
    @NetworkScope
    fun provideDoctorCategory(
        @Doctor nearbySearchApi: IJsoupNearbySearchApi,
        @Doctor findApi: IJsoupFindApi
    ): SearchItemCategory.Doctor {
        SearchItemCategory.Doctor.nearbySearchApi = nearbySearchApi
        SearchItemCategory.Doctor.findApi = findApi
        return SearchItemCategory.Doctor
    }

    @Provides
    @NetworkScope
    fun provideLpuCategory(
        @Lpu nearbySearchApi: IJsoupNearbySearchApi,
        @Lpu findApi: IJsoupFindApi
    ): SearchItemCategory.Lpu {
        SearchItemCategory.Lpu.nearbySearchApi = nearbySearchApi
        SearchItemCategory.Lpu.findApi = findApi
        return SearchItemCategory.Lpu
    }

    @Provides
    @NetworkScope
    fun provideServicesCategory(
        @Service nearbySearchApi: IJsoupNearbySearchApi,
        @Service findApi: IJsoupFindApi
    ): SearchItemCategory.Services {
        SearchItemCategory.Services.nearbySearchApi = nearbySearchApi
        SearchItemCategory.Services.findApi = findApi
        return SearchItemCategory.Services
    }

    @Provides
    @NetworkScope
    fun provideSearchItemCategories(): List<SearchItemCategory> =
        SearchItemCategory.values

}