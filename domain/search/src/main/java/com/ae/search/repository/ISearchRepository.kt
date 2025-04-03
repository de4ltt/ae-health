package com.ae.search.repository

import com.ae.search.model.ISearchItem
import com.ae.search.model.SearchParams
import kotlinx.coroutines.flow.Flow

interface ISearchRepository {
    fun search(searchParams: SearchParams): Flow<List<ISearchItem>>
}