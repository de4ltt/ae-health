package com.ae.search.repository

import com.ae.network.model.NetworkRequestResult
import com.ae.search.model.interfaces.ISearchItem
import com.ae.search.model.search.SearchParams

interface ISearchRepository {
    suspend fun searchWithFilters(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>>
    suspend fun searchNearby(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>>
    suspend fun searchServiceTypes(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>>
}