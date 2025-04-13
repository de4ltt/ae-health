package com.ae.network.jsoup

import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.model.SearchParamsNetwork
import com.ae.network_request.NetworkRequestResult

internal interface IJsoupFindApi {
    suspend fun find(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun findClinics(query: String): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun findServices(query: String): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun findDoctors(query: String): NetworkRequestResult<List<TypedItemResponse>>
}