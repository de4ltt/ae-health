package com.ae.search.search_function.search

import com.ae.network.handler.handleNetworkRequest
import com.ae.network.model.NetworkRequestResult
import com.ae.search.dto.retrofit.LocatedItemResponse
import com.ae.search.dto.retrofit.TypedItemResponse
import com.ae.search.model.CoordinatedArea
import com.ae.search.retrofit.IMapSearchApi

internal suspend fun searchForServices(
    service: String,
    coordinatedArea: CoordinatedArea,
    searchApi: IMapSearchApi
): List<LocatedItemResponse> {

    return try {
        val results = mutableListOf<TypedItemResponse>()

        val result = handleNetworkRequest {
            searchApi.findByService(service = service, bbox = coordinatedArea.toString())
        }

        if (result is NetworkRequestResult.Success)
            result.data.toLocatedItems()
        else
            throw ((result as NetworkRequestResult.Error).error)
    } catch (e: Throwable) {
        emptyList<LocatedItemResponse>()
    }

}