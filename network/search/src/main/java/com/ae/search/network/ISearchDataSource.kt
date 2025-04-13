package com.ae.search.network

import com.ae.network.model.NetworkRequestResult
import com.ae.search.network.dto.retrofit.TypedItemResponse
import com.ae.search.network.model.SearchParamsNetwork

interface ISearchDataSource {
    suspend fun searchWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun searchNearbyWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun searchServiceTypes(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>>
}