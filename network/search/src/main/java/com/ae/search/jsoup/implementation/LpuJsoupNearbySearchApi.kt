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

internal class LpuJsoupNearbySearchApi : IJsoupNearbySearchApi() {

    override suspend fun search(
        query: String,
        coordinatedArea: CoordinatedArea,
        searchApi: IMapSearchApi
    ): NetworkRequestResult<List<TypedItemResponse>> {

        val results = mutableListOf<TypedItemResponse>()

        val result = handleNetworkRequest(
            request = {
                searchApi.findByLpuType(
                    lpuType = query,
                    bbox = coordinatedArea.toString()
                )
            },
            transform = FeatureCollectionResponse::toTypedItems
        )

        return result

    }

    override suspend fun select(doc: Document): NetworkRequestResult<List<TypedItemResponse>> {

        return try {

            val lpuElements = doc.select("div.b-section-box__elem.d-flex.py-6")

            val results: MutableList<TypedItemResponse> = mutableListOf()

            for (lpuElement in lpuElements) {
                val lpu = TypedItemResponse(
                    title = lpuElement.selectFirst("span[data-qa=\"lpu_card_heading_lpu_name\"]")!!
                        .text()
                        .trim(),
                    subtitle = lpuElement.selectFirst("span[data-qa=\"lpu_card_btn_addr_text\"]")!!
                        .text()
                        .trim(),
                    image = lpuElement.selectFirst("img[data-qa=\"lpu_card_logo_image\"]")!!
                        .attr("src"),
                    category = "LPU",
                    link = lpuElement.selectFirst("a[data-qa=\"lpu_card_heading\"]")!!.attr("href"),
                )

                results += lpu
            }

            NetworkRequestResult.Success(results)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }
    }

    override suspend fun searchType(query: String): NetworkRequestResult<List<TypedItemResponse>> {

        return try {
            val uri = "https://prodoctorov.ru/krasnodar/find/?q=$query&filter=lputypes"

            val doc = Jsoup.connect(uri).get()

            val lpuTypeElements = doc.select("a.b-list-icon-link.b-section-box__elem")

            val results = mutableListOf<TypedItemResponse>()

            lpuTypeElements.forEach { lpuTypeElement ->
                val lpuType = TypedItemResponse(
                    title = lpuTypeElement.selectFirst("span.b-list-icon-link__text.ui-text.ui-text_body-1")!!
                        .text().trim(),
                    category = "LPU_TYPE",
                    link = lpuTypeElement.attr("href")
                )

                results += lpuType
            }

            NetworkRequestResult.Success(results)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }

    }

}