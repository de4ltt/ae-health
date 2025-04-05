package com.ae.search.repository

import com.ae.search.model.ISearchItem
import com.ae.search.model.SearchParams
import kotlinx.coroutines.flow.Flow

interface ISearchRepository {
    suspend fun searchWithFilters(searchParams: SearchParams): List<ISearchItem>
    suspend fun searchNearby(searchParams: SearchParams): List<ISearchItem>
}