package com.ae.network.search_function

import com.ae.network.dto.retrofit.LocatedItemResponse
import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.model.CoordinatedArea
import com.ae.network_request.NetworkRequestResult
import com.ae.network_request.handleNetworkRequest
import com.ae.network.retrofit.IMapSearchApi

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