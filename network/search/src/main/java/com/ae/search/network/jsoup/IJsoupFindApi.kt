package com.ae.search.network.jsoup

import com.ae.network.model.NetworkRequestResult
import com.ae.search.network.dto.retrofit.TypedItemResponse
import com.ae.search.network.model.SearchParamsNetwork

internal interface IJsoupFindApi {
    suspend fun find(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun findClinics(query: String): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun findServices(query: String): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun findDoctors(query: String): NetworkRequestResult<List<TypedItemResponse>>
}