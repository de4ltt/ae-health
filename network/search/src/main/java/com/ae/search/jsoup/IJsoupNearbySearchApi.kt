package com.ae.search.jsoup

import com.ae.network.handler.handleNetworkRequest
import com.ae.network.model.NetworkRequestResult
import com.ae.search.dto.retrofit.FeatureCollectionResponse
import com.ae.search.model.TypedItemResponse
import com.ae.search.model.CoordinatedArea
import com.ae.search.retrofit.IMapSearchApi
import org.jsoup.nodes.Document

internal abstract class IJsoupNearbySearchApi {

    suspend fun find(
        coordinatedArea: CoordinatedArea,
        searchApi: IMapSearchApi
    ): NetworkRequestResult<List<TypedItemResponse>> {

        val result = handleNetworkRequest(
            request = { searchApi.find(bbox = coordinatedArea.toString()) },
            transform = FeatureCollectionResponse::toTypedItems
        )

        return result
    }

    abstract suspend fun search(
        query: String,
        coordinatedArea: CoordinatedArea,
        searchApi: IMapSearchApi
    ): NetworkRequestResult<List<TypedItemResponse>>

    abstract suspend fun select(doc: Document): NetworkRequestResult<List<TypedItemResponse>>
    abstract suspend fun searchType(query: String): NetworkRequestResult<List<TypedItemResponse>>
}