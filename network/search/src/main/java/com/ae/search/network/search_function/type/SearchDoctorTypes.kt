package com.ae.search.network.search_function.type

import com.ae.network.dto.retrofit.TypedItemResponse
import org.jsoup.Jsoup

internal fun searchDoctorTypes(
    doctorType: String
): List<TypedItemResponse> {
    return try {
        val uri = "https://prodoctorov.ru/krasnodar/find/?q=$doctorType&filter=specialities"

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

        println(results)

        results
    } catch (e: Throwable) {
        emptyList<TypedItemResponse>()
    }
}