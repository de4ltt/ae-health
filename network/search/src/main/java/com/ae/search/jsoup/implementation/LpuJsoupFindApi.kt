package com.ae.search.jsoup.implementation

import com.ae.config.di.annotation.SecretProperty
import com.ae.network.model.NetworkRequestError
import com.ae.network.model.NetworkRequestResult
import com.ae.search.model.TypedItemResponse
import com.ae.search.jsoup.IJsoupFindApi
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

internal class LpuJsoupFindApi @Inject constructor(
    @SecretProperty("find_url") private val findUrl: String
): IJsoupFindApi(findUrl) {

    override suspend fun findMedicine(query: String): NetworkRequestResult<List<TypedItemResponse>> {
        val uri = "$findUrl/?q=$query&filter=lpus"

        return try {

            val doc: Document = Jsoup.connect(uri)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()

            val results: MutableList<TypedItemResponse> = mutableListOf()

            val clinicElements = doc.select("div.b-section-box__elem.d-flex.py-6")

            println(clinicElements)

            clinicElements.forEach { clinicElement ->

                val clinic = TypedItemResponse(
                    title = clinicElement.selectFirst("span[data-qa=\"lpu_card_heading_lpu_name\"]")!!
                        .text().trim(),
                    subtitle = clinicElement.selectFirst("span[data-qa=\"lpu_card_btn_addr_text\"]")!!
                        .text().trim(),
                    category = "LPU",
                    image = clinicElement.selectFirst("img[data-qa=\"lpu_card_logo_image\"]")!!
                        .attr("src"),
                    link = clinicElement.selectFirst("a[data-qa=\"lpu_card_heading\"]")!!
                        .attr("href")
                )

                results += clinic
            }

            NetworkRequestResult.Success(results)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }
    }

}