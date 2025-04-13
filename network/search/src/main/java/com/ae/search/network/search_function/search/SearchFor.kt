package com.ae.search.network.search_function.search

import com.ae.network.handler.handleNetworkRequest
import com.ae.network.model.NetworkRequestResult
import com.ae.search.network.dto.retrofit.LocatedItemResponse
import com.ae.search.network.dto.retrofit.TypedItemResponse
import com.ae.search.network.model.CoordinatedArea
import com.ae.search.network.retrofit.IMapSearchApi

internal suspend fun searchFor(
    coordinatedArea: CoordinatedArea,
    searchApi: IMapSearchApi
): List<LocatedItemResponse> {

    return try {
        val results = mutableListOf<TypedItemResponse>()

        val result = handleNetworkRequest {
            searchApi.find(bbox = coordinatedArea.toString())
        }

        if (result is NetworkRequestResult.Success)
            result.data.toLocatedItems()
        else
            throw ((result as NetworkRequestResult.Error).error)
    } catch (e: Throwable) {
        emptyList<LocatedItemResponse>()
    }

}