package com.ae.network

import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.dto.retrofit.TypedItemsResponse
import com.ae.network.model.CoordinatedArea
import com.ae.network.model.NetworkRequestResult
import com.ae.network.model.SearchParamsNetwork

interface ISearchDataSource {
    suspend fun searchWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>>
    suspend fun searchNearbyWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>>
}