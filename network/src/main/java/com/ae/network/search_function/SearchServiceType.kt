package com.ae.network.search_function

import com.ae.network.dto.retrofit.TypedItemResponse
import org.jsoup.Jsoup

internal fun searchServiceType(
    serviceType: String
): List<TypedItemResponse> {

    return try {
        val uri = "https://prodoctorov.ru/krasnodar/find/?q=$serviceType&filter=services"

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

        results
    } catch (e: Throwable) {
        emptyList<TypedItemResponse>()
    }
}