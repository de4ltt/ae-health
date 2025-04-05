package com.ae.network.retrofit

import com.ae.network.dto.TypedItemsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private const val DEFAULT_TOWN = "krasnodar"

interface SearchDataApi {
    @POST
    fun searchWithFilters(
        @Query("query") query: String,
        @Query("town") town: String = DEFAULT_TOWN
    ): Response<List<TypedItemsDTO>>
}