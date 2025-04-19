package com.ae.search.jsoup.implementation

import com.ae.config.di.annotation.SecretProperty
import com.ae.network.model.NetworkRequestError
import com.ae.network.model.NetworkRequestResult
import com.ae.search.model.TypedItemResponse
import com.ae.search.jsoup.IJsoupFindApi
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

internal class ServiceJsoupFindApi @Inject constructor(
    @SecretProperty("find_url") private val findUrl: String
) : IJsoupFindApi(findUrl) {

    override suspend fun findMedicine(query: String): NetworkRequestResult<List<TypedItemResponse>> {
        val uri = "$findUrl/?q=$query&filter=services"

        return try {

            val doc: Document = Jsoup.connect(uri)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()

            println(doc)

            val results: MutableList<TypedItemResponse> = mutableListOf()

            val serviceElements =
                doc.select("a.b-list-icon-link.b-section-box__elem")

            println(serviceElements)

            serviceElements.forEach { serviceElement ->

                val service = TypedItemResponse(
                    title = serviceElement.selectFirst("span.b-list-icon-link__text.ui-text.ui-text_body-1")!!
                        .text().trim(),
                    category = "SERVICES",
                    link = serviceElement.attr("href")
                )

                results += service
            }

            NetworkRequestResult.Success(results)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }
    }

}