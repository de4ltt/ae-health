package com.ae.network.retrofit

import com.ae.network.dto.retrofit.TypedItemsResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

private const val DEFAULT_TOWN = "krasnodar"

internal interface SearchDataApi {
    @POST("search")
    fun searchWithFilters(
        @Query("query") query: String,
        @Query("town") town: String = DEFAULT_TOWN
    ): Response<List<TypedItemsResponse>>
}