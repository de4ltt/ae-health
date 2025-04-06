package com.ae.search.repository

import com.ae.network.ISearchDataSource
import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.model.NetworkRequestResult
import com.ae.search.mapper.toDomain
import com.ae.search.mapper.toNetwork
import com.ae.search.model.ISearchItem
import com.ae.search.model.SearchParams

internal class SearchRepository(
    private val searchDataSource: ISearchDataSource
) : ISearchRepository {

    override suspend fun searchWithFilters(searchParams: SearchParams): List<ISearchItem> {
        val response = searchDataSource.searchWithFilters(searchParams.toNetwork())

        if (response is NetworkRequestResult.Success<List<TypedItemResponse>>)
            return response.data.toDomain()
        else
            throw ((response as NetworkRequestResult.Error).error)
    }

    override suspend fun searchNearby(searchParams: SearchParams): List<ISearchItem> {
        val response = searchDataSource.searchNearbyWithFilters(searchParams.toNetwork())

        if (response is NetworkRequestResult.Success<List<TypedItemResponse>>)
            return response.data.toDomain()
        else
            throw ((response as NetworkRequestResult.Error).error)
    }
}