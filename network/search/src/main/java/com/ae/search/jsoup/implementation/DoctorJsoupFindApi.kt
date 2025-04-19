package com.ae.search.jsoup.implementation

import com.ae.config.di.annotation.SecretProperty
import com.ae.network.model.NetworkRequestError
import com.ae.network.model.NetworkRequestResult
import com.ae.search.model.TypedItemResponse
import com.ae.search.jsoup.IJsoupFindApi
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

internal class DoctorJsoupFindApi @Inject constructor(
    @SecretProperty("find_url") private val findUrl: String
): IJsoupFindApi(findUrl) {

    override suspend fun findMedicine(query: String): NetworkRequestResult<List<TypedItemResponse>> {
        val uri = "$findUrl/?q=$query&filter=doctors"

        return try {

            val doc: Document = Jsoup.connect(uri)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()

            val results: MutableList<TypedItemResponse> = mutableListOf()

            val doctorElements =
                doc.select("article[class=\"b-card b-card_list-item b-section-box__elem\"]")

            doctorElements.forEach { doctorElement ->

                val doctor = TypedItemResponse(
                    title = doctorElement.selectFirst("a.b-card__name-link.b-link.ui-text.ui-text_h6.ui-kit-color-text")!!
                        .text().trim(),
                    subtitle = doctorElement.selectFirst("p.b-card__category.ui-text.ui-text_body-1.ui-kit-color-text-secondary")!!
                        .text().trim(),
                    category = "DOCTOR",
                    image = doctorElement.selectFirst("img.b-card__avatar-img")!!
                        .attr("src"),
                    link = doctorElement.selectFirst("a.b-card__name-link.b-link.ui-text.ui-text_h6.ui-kit-color-text")!!
                        .attr("href")
                )

                results += doctor
            }

            println("MY RESULTS: $results")

            NetworkRequestResult.Success(results)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }
    }

}