package com.ae.network.search_function

import com.ae.network.handler.handleNetworkRequest
import com.ae.network.model.NetworkRequestResult
import com.ae.search.dto.retrofit.LocatedItemResponse
import com.ae.search.dto.retrofit.TypedItemResponse
import com.ae.search.model.CoordinatedArea
import com.ae.search.retrofit.IMapSearchApi

internal suspend fun searchForLpu(
    lpuType: String,
    coordinatedArea: CoordinatedArea,
    searchApi: IMapSearchApi
): List<LocatedItemResponse> {

    return try {
        val results = mutableListOf<TypedItemResponse>()

        val result = handleNetworkRequest {
            searchApi.findByLpuType(lpuType = lpuType, bbox = coordinatedArea.toString())
        }

        if (result is NetworkRequestResult.Success)
            result.data.toLocatedItems()
        else
            throw ((result as NetworkRequestResult.Error).error)
    } catch (e: Throwable) {
        emptyList<LocatedItemResponse>()
    }

}