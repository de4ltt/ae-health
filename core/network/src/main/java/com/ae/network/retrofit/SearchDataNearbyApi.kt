package com.ae.network.retrofit

import retrofit2.http.POST
import retrofit2.http.Query

interface SearchDataNearbyApi {
    @POST
    fun searchNearby(
        @Query("bbox") bbox: String
    ): List<>
}