package com.ae.search.data_source

import com.ae.annotations.IoDispatcher
import com.ae.network.handler.handleNetworkRequests
import com.ae.network.model.NetworkRequestError
import com.ae.network.model.NetworkRequestResult
import com.ae.search.ISearchDataSource
import com.ae.search.model.CoordinatedArea
import com.ae.search.model.SearchParamsNetwork
import com.ae.search.model.TypedItemResponse
import com.ae.search.model.SearchItemCategory
import com.ae.search.retrofit.IMapSearchApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SearchDataSource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val mapSearchApi: IMapSearchApi
) : ISearchDataSource {

    override suspend fun searchWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> =
        withContext(ioDispatcher) {

            val result = if (searchParams.itemFilters.isEmpty())
                SearchItemCategory.Lpu.findApi.find(searchParams)
            else handleNetworkRequests(
                requests = List(size = searchParams.itemFilters.size) { index ->
                    async {
                        searchParams.itemFilters[index].findApi.findMedicine(
                            searchParams.query
                        )
                    }
                }
            )

            result
        }

    override suspend fun searchNearbyWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> =
        withContext(ioDispatcher) {
            try {
                if (searchParams.itemFilters.size > 1 || searchParams.radius == null)
                    throw NetworkRequestError.UnknownError("Incorrect input")

                val coordinatedArea =
                    CoordinatedArea(searchParams.lat, searchParams.lon, searchParams.radius)

                val results = (if (searchParams.itemFilters.isEmpty())
                    SearchItemCategory.Lpu.nearbySearchApi.find(
                        coordinatedArea = coordinatedArea,
                        searchApi = mapSearchApi
                    )
                else
                    searchParams.itemFilters[0].nearbySearchApi.search(
                        searchParams.query,
                        coordinatedArea,
                        mapSearchApi
                    ))

                results
            } catch (e: NetworkRequestError) {
                NetworkRequestResult.Error(e)
            } catch (e: Throwable) {
                NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
            }
        }

    override suspend fun searchServiceTypes(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> =
        withContext(ioDispatcher) {
            try {
                if (searchParams.itemFilters.size != 1 || searchParams.radius == null)
                    throw NetworkRequestError.UnknownError("Incorrect input")

                val results = searchParams.itemFilters[0]
                    .nearbySearchApi.searchType(
                        searchParams.query,
                    )

                results
            } catch (e: NetworkRequestError) {
                NetworkRequestResult.Error(e)
            } catch (e: Throwable) {
                NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
            }
        }
}