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

internal class DoctorJsoupNearbySearchApi : IJsoupNearbySearchApi() {

    override suspend fun search(
        query: String,
        coordinatedArea: CoordinatedArea,
        searchApi: IMapSearchApi
    ): NetworkRequestResult<List<TypedItemResponse>> {

        val result = handleNetworkRequest(
            request = {
                searchApi.findBySpeciality(
                    speciality = query,
                    bbox = coordinatedArea.toString()
                )
            },
            transform = FeatureCollectionResponse::toTypedItems
        )

        return result
    }

    override suspend fun select(doc: Document): NetworkRequestResult<List<TypedItemResponse>> {

        return try {
            val doctorElements = doc.select("article.b-card.b-card_list-item.b-section-box__elem")

            val results: MutableList<TypedItemResponse> = mutableListOf()

            for (doctorElement in doctorElements) {
                val doctor = TypedItemResponse(
                    title = doctorElement.selectFirst("a.b-card__name-link.b-link.ui-text.ui-text_h6.ui-kit-color-text")!!
                        .text(),
                    subtitle = doctorElement.selectFirst("p.b-card__category.ui-text.ui-text_body-1.ui-kit-color-text-secondary")!!
                        .text(),
                    category = "DOCTOR",
                    image = doctorElement.selectFirst("img.b-card__avatar-img")!!.attr("src"),
                    link = doctorElement.selectFirst("a.b-card__name-link.b-link.ui-text.ui-text_h6.ui-kit-color-text")!!
                        .attr("href"),
                )

                results += doctor
            }

            NetworkRequestResult.Success(results)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }

    }

    override suspend fun searchType(query: String): NetworkRequestResult<List<TypedItemResponse>> {

        return try {
            val uri = "https://prodoctorov.ru/krasnodar/find/?q=$query&filter=specialities"

            val doc = Jsoup.connect(uri).get()

            val doctorTypeElements = doc.select("a.b-list-icon-link.b-section-box__elem")

            val results = mutableListOf<TypedItemResponse>()

            doctorTypeElements.forEach { doctorTypeElement ->
                val doctorType = TypedItemResponse(
                    title = doctorTypeElement.selectFirst("span.b-list-icon-link__text.ui-text.ui-text_body-1")!!
                        .text().trim(),
                    category = "DOCTOR_TYPE",
                    link = doctorTypeElement.attr("href")
                )

                results += doctorType
            }

            NetworkRequestResult.Success(results)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }

    }

}