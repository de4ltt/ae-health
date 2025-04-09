package com.ae.network.jsoup

import com.ae.network.dto.retrofit.TypedItemResponse

interface IJsoupFindApi {
    suspend fun find(query: String): List<TypedItemResponse>
    suspend fun findClinics(query: String): List<TypedItemResponse>
    suspend fun findServices(query: String): List<TypedItemResponse>
    suspend fun findDoctors(query: String): List<TypedItemResponse>
}