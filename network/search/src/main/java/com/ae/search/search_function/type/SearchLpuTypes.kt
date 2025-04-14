package com.ae.search.search_function.type

import com.ae.search.dto.retrofit.TypedItemResponse
import org.jsoup.Jsoup

internal fun searchLpuTypes(
    lpuType: String
): List<TypedItemResponse> {
    return try {
        val uri = "https://prodoctorov.ru/krasnodar/find/?q=$lpuType&filter=lputypes"

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

        results
    } catch (e: Throwable) {
        emptyList<TypedItemResponse>()
    }
}