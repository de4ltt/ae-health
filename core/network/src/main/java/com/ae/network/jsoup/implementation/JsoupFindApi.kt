package com.ae.network.jsoup.implementation

import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.jsoup.IJsoupFindApi
import com.ae.network.model.ISecretProperties
import com.ae.network.model.SearchParamsNetwork
import com.ae.network.request_result.NetworkRequestError
import com.ae.network.request_result.NetworkRequestResult
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

internal class JsoupFindApi @Inject constructor(
    private val secretProperties: ISecretProperties
) : IJsoupFindApi {

    override suspend fun find(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> {
        val uri = "${secretProperties.defaultUri}/krasnodar/find/?q=${searchParams.query}"

        return try {

            val doc: Document = Jsoup.connect(uri)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()

            val results: MutableList<TypedItemResponse> = mutableListOf()

            searchParams.itemFilters.forEach { category ->
                results.addAll(category.selectFunction(doc))
            }

            NetworkRequestResult.Success(results)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }
    }

    override suspend fun findDoctors(query: String): NetworkRequestResult<List<TypedItemResponse>> {
        val uri = "${secretProperties.defaultUri}/krasnodar/find/?q=$query&filter=doctors"

        return try {

            val doc: Document = Jsoup.connect(uri)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()

            val results: MutableList<TypedItemResponse> = mutableListOf()

            val doctorElements = doc.select("article[class=\"b-card b-card_list-item b-section-box__elem\"]")

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

    override suspend fun findServices(query: String): NetworkRequestResult<List<TypedItemResponse>> {
        val uri = "${secretProperties.defaultUri}/krasnodar/find/?q=$query&filter=services"

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
                    title = serviceElement.selectFirst("span.b-list-icon-link__text.ui-text.ui-text_body-1")!!.text().trim(),
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

    override suspend fun findClinics(query: String): NetworkRequestResult<List<TypedItemResponse>> {
        val uri = "${secretProperties.defaultUri}/krasnodar/find/?q=$query&filter=lpus"

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