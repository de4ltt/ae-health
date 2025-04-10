package com.ae.network.data_source

import com.ae.annotations.IoDispatcher
import com.ae.network.ISearchDataSource
import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.jsoup.IJsoupFindApi
import com.ae.network.jsoup.IJsoupMapApi
import com.ae.network.model.CoordinatedArea
import com.ae.network.request_result.NetworkRequestError
import com.ae.network.request_result.NetworkRequestResult
import com.ae.network.model.SearchParamsNetwork
import com.ae.network.retrofit.ISearchApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SearchDataSource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val jsoupMapApi: IJsoupMapApi,
    private val jsoupFindApi: IJsoupFindApi,
    private val searchApi: ISearchApi,
) : ISearchDataSource {

    override suspend fun searchWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> =
        withContext(ioDispatcher) {
//            val result = handleNetworkRequest {
//                searchApi.searchWithFilters(query = searchParams.query)
//            }

            val result = jsoupFindApi.find(searchParams)

            println(searchParams)
            println(result.toString())

            if (result is NetworkRequestResult.Success)
                NetworkRequestResult.Success(
                    result.data
                )
            else
                result as NetworkRequestResult.Error

//                NetworkRequestResult.Success(result.data.mapNotNull {
//                    if (searchParams.itemFilters.map { it.toString() }.contains(
//                            it.title.uppercase()
//                        )
//                    ) it else null
//                })

        }

    override suspend fun searchNearbyWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> =
        withContext(ioDispatcher) {

            val results: MutableList<TypedItemResponse> = mutableListOf()

            val coordinatedArea = CoordinatedArea(
                lat = searchParams.lat,
                lon = searchParams.lon,
                radius = searchParams.radius!!
            )

            val mapResult = handleNetworkRequest {
                searchApi.searchNearby(bbox = coordinatedArea.toString())
            }

            if (mapResult is NetworkRequestResult.Success) {

                var orgsLinks = mapResult.data.toLocatedItems().mapNotNull {

                    val res = jsoupMapApi.findClinic(query = it.name, lat = it.lat, lon = it.lon)

                    if (res is NetworkRequestResult.Error) null
                    else (res as NetworkRequestResult.Success).data
                }

                searchParams.itemFilters.forEach { param ->
                    results += param.searchFunction(searchParams, orgsLinks, jsoupMapApi)
                }

                NetworkRequestResult.Success(results.toList())

            } else NetworkRequestResult.Error(NetworkRequestError.UnknownError())

        }
}