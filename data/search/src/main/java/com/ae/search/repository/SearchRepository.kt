package com.ae.search.repository

import com.ae.network.ISearchDataSource
import com.ae.network.request_result.NetworkRequestResult
import com.ae.network_request.NetworkRequestError
import com.ae.network_request.NetworkRequestResult
import com.ae.search.mapper.toDomain
import com.ae.search.mapper.toNetwork
import com.ae.search.model.ISearchItem
import com.ae.search.model.SearchParams
import javax.inject.Inject

internal class SearchRepository @Inject constructor(
    private val searchDataSource: ISearchDataSource
) : ISearchRepository {

    override suspend fun searchWithFilters(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>> {
        val response = searchDataSource.searchWithFilters(searchParams.toNetwork())

        if (response is NetworkRequestResult.Success)
            return NetworkRequestResult.Success(response.data.toDomain())
        else
            response as NetworkRequestResult.Error
    }

    override suspend fun searchNearby(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>> {
        val response = searchDataSource.searchNearbyWithFilters(searchParams.toNetwork())

        if (response is NetworkRequestResult.Success)
            return NetworkRequestResult.Success(response.data.toDomain())
        else
            response as NetworkRequestResult.Error
    }

    override suspend fun searchServiceTypes(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>> {
        val response = searchDataSource.searchServiceTypes(searchParams.toNetwork())

        if (response is NetworkRequestResult.Success)
            return NetworkRequestResult.Success(response.data.toDomain())
        else
            response as NetworkRequestResult.Error
    }
}