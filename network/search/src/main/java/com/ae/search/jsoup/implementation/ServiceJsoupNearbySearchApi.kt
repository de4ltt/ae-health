package com.ae.search.jsoup.implementation

import com.ae.network.handler.handleNetworkRequest
import com.ae.network.model.NetworkRequestError
import com.ae.network.model.NetworkRequestResult
import com.ae.search.dto.retrofit.FeatureCollectionResponse
import com.ae.search.model.TypedItemResponse
import com.ae.search.jsoup.IJsoupNearbySearchApi
import com.ae.search.model.CoordinatedArea
import com.ae.search.retrofit.IMapSearchApi
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

internal class ServiceJsoupNearbySearchApi : IJsoupNearbySearchApi() {

    override suspend fun search(
        query: String,
        coordinatedArea: CoordinatedArea,
        searchApi: IMapSearchApi
    ): NetworkRequestResult<List<TypedItemResponse>> {

        val results = mutableListOf<TypedItemResponse>()

        val result = handleNetworkRequest(
            request = {
                searchApi.findByService(
                    service = query,
                    bbox = coordinatedArea.toString()
                )
            },
            transform = FeatureCollectionResponse::toTypedItems
        )

        return result
    }

    override suspend fun select(doc: Document): NetworkRequestResult<List<TypedItemResponse>> {

        return try {
            val serviceElements = doc.select("a.b-list-icon-link.b-section-box__elem")

            val results: MutableList<TypedItemResponse> = mutableListOf()

            for (serviceElement in serviceElements) {
                val service = TypedItemResponse(
                    title = serviceElement.selectFirst("span.b-list-icon-link__text.ui-text.ui-text_body-1]")!!
                        .text().trim(),
                    link = serviceElement.attr("href"),
                    category = "SERVICES"
                )

                results += service
            }

            NetworkRequestResult.Success(results)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }

    }

    override suspend fun searchType(query: String): NetworkRequestResult<List<TypedItemResponse>> {
        return try {
            val uri = "https://prodoctorov.ru/krasnodar/find/?q=$query&filter=services"

            val doc = Jsoup.connect(uri).get()

            val serviceTypeElements = doc.select("a.b-list-icon-link.b-section-box__elem")

            val results = mutableListOf<TypedItemResponse>()

            serviceTypeElements.forEach { serviceTypeElement ->
                val serviceType = TypedItemResponse(
                    title = serviceTypeElement.selectFirst("span.b-list-icon-link__text.ui-text.ui-text_body-1")!!
                        .text().trim(),
                    category = "SERVICE_TYPE",
                    link = serviceTypeElement.attr("href")
                )

                results += serviceType
            }

            NetworkRequestResult.Success(results)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }
    }

}