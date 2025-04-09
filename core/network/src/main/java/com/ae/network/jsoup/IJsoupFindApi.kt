package com.ae.network.jsoup

import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.jsoup.`typealias`.FoundElements
import com.ae.network.model.SearchParamsNetwork
import com.ae.network.request_result.NetworkRequestResult

interface IJsoupFindApi {
    suspend fun find(query: SearchParamsNetwork): NetworkRequestResult<FoundElements>
    suspend fun findClinics(query: String): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun findServices(query: String): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun findDoctors(query: String): NetworkRequestResult<List<TypedItemResponse>>
}