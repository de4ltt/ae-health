package com.ae.search.jsoup

import com.ae.network.model.NetworkRequestResult
import com.ae.search.dto.retrofit.TypedItemResponse
import com.ae.search.model.SearchParamsNetwork

internal interface IJsoupFindApi {
    suspend fun find(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun findClinics(query: String): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun findServices(query: String): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun findDoctors(query: String): NetworkRequestResult<List<TypedItemResponse>>
}