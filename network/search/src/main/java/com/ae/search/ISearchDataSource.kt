package com.ae.search

import com.ae.network.model.NetworkRequestResult
import com.ae.search.model.TypedItemResponse
import com.ae.search.model.SearchParamsNetwork

interface ISearchDataSource {
    suspend fun searchWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun searchNearbyWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun searchServiceTypes(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>>
}