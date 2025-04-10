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

            for (category in searchParams.itemFilters) {
                results.addAll(category.selectFunction(doc))
                println(results.size)
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

            val doctorElements = doc.select("div.b-results__item")

            for (doctorElement in doctorElements) {
                val doctor = TypedItemResponse(
                    title = doctorElement.selectFirst("div.ui-text.ui-text_subtitle-1.ui-kit-color-primary.mb-1")!!
                        .text().trim(),
                    subtitle = doctorElement.selectFirst("ui-text.ui-text_body-2.ui-kit-color-text-info.mb-2")!!
                        .text().trim(),
                    category = "DOCTOR",
                    image = doctorElement.selectFirst("img.b-avatar.b-avatar_xl.b-avatar_border mr-4")!!
                        .attr("src"),
                    link = doctorElement.selectFirst("div.b-results__redirect.b-card.b-card_reset-radius.b-card_border_new-grey.b-card_padding_normal.px-2.pt-6.pb-4")!!
                        .attr("data-redirect-path")
                )

                results += doctor
            }

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

            val results: MutableList<TypedItemResponse> = mutableListOf()

            val serviceElements =
                doc.select("a.b-results__link.b-link.ui-text.ui-text_body-1.ui-kit-color-text")

            for (serviceElement in serviceElements) {
                val service = TypedItemResponse(
                    title = serviceElement.text().trim(),
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

            val clinicElements = doc.select("div.b-card.pb-4")

            for (clinicElement in clinicElements) {
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