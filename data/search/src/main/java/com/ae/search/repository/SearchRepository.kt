package com.ae.search.repository

import com.ae.network.model.NetworkRequestResult
import com.ae.network.handler.handleResult
import com.ae.search.mapper.toDomain
import com.ae.search.mapper.toNetwork
import com.ae.search.model.interfaces.ISearchItem
import com.ae.search.model.search.SearchParams
import com.ae.search.network.ISearchDataSource
import com.ae.search.network.dto.retrofit.TypedItemResponse
import javax.inject.Inject

internal class SearchRepository @Inject constructor(
    private val searchDataSource: ISearchDataSource
) : ISearchRepository {

    override suspend fun searchWithFilters(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>> =
        searchDataSource.searchWithFilters(searchParams.toNetwork())
            .handleResult(List<TypedItemResponse>::toDomain)

    override suspend fun searchNearby(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>> =
        searchDataSource.searchNearbyWithFilters(searchParams.toNetwork())
            .handleResult(List<TypedItemResponse>::toDomain)

    override suspend fun searchServiceTypes(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>> =
        searchDataSource.searchServiceTypes(searchParams.toNetwork())
            .handleResult(List<TypedItemResponse>::toDomain)

}