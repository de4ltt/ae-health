package com.ae.network.retrofit

import com.ae.network.dto.retrofit.FeatureCollectionResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

internal interface SearchDataNearbyApi {
    @POST
    fun searchNearby(
        @Query("bbox") bbox: String
    ): Response<FeatureCollectionResponse>
}