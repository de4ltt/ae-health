package com.ae.network

import com.ae.network.dto.TypedItemsDTO
import com.ae.network.model.NetworkRequestResult
import com.ae.network.model.SearchParamsNetwork

interface ISearchDataSource {
    suspend fun searchWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemsDTO>>
    suspend fun searchNearby(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemsDTO>>
}