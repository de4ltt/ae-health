package com.ae.search.network.data_source

import com.ae.annotations.IoDispatcher
import com.ae.network.ISearchDataSource
import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.jsoup.IJsoupFindApi
import com.ae.network.search_function.searchFor
import com.ae.network.mapper.toTypedItems
import com.ae.network.model.CoordinatedArea
import com.ae.network_request.NetworkRequestError
import com.ae.network_request.NetworkRequestResult
import com.ae.network.model.SearchParamsNetwork
import com.ae.network.retrofit.IMapSearchApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SearchDataSource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val jsoupFindApi: IJsoupFindApi,
    private val mapSearchApi: IMapSearchApi
) : ISearchDataSource {

    override suspend fun searchWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> =
        withContext(ioDispatcher) {

            //val result = jsoupFindApi.find(searchParams)

            val results = listOf(
                async { jsoupFindApi.findDoctors("Гор") },
                async { jsoupFindApi.findClinics("Гор") },
                async { jsoupFindApi.findServices("Анализ") }
            ).awaitAll().filter { it is NetworkRequestResult.Success }
                .flatMap { (it as NetworkRequestResult.Success).data }

            if (/*result is NetworkRequestResult.Success*/true)
                NetworkRequestResult.Success(
                    results
                )
            else
                NetworkRequestResult.Error(NetworkRequestError.UnknownError())

        }

    override suspend fun searchNearbyWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> =
        withContext(ioDispatcher) {
            try {
                if (searchParams.itemFilters.size > 1 || searchParams.radius == null)
                    throw NetworkRequestError.UnknownError("Incorrect input")

                val coordinatedArea =
                    CoordinatedArea(searchParams.lat, searchParams.lon, searchParams.radius)

                val results = (if (searchParams.itemFilters.isEmpty())
                    searchFor(coordinatedArea, mapSearchApi)
                else
                    searchParams.itemFilters[0]
                        .searchFunction(
                            searchParams.query,
                            coordinatedArea,
                            mapSearchApi
                        )).toTypedItems()

                NetworkRequestResult.Success(results)
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
                    .searchTypeFunction(
                        searchParams.query,
                    )

                NetworkRequestResult.Success(results)
            } catch (e: Throwable) {
                NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
            }
        }
}