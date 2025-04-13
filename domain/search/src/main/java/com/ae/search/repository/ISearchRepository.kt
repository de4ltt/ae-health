package com.ae.search.repository

import com.ae.network_request.NetworkRequestResult
import com.ae.search.model.interfaces.ISearchItem
import com.ae.search.model.search.SearchParams
import kotlinx.coroutines.flow.Flow

interface ISearchRepository {
    suspend fun searchWithFilters(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>>
    suspend fun searchNearby(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>>
    suspend fun searchServiceTypes(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>>
}