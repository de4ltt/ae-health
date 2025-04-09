package com.ae.network.jsoup.search_function

import com.ae.network.dto.retrofit.TypedItemResponse
import org.jsoup.nodes.Document

internal suspend fun selectServices(
    doc: Document
): Pair<String, List<TypedItemResponse>>? {

    val serviceElements = doc.select("a.b-list-icon-link.b-section-box__elem")

    val results: MutableList<TypedItemResponse> = mutableListOf()

    for (serviceElement in serviceElements) {
        val service = TypedItemResponse(
            title = serviceElement.selectFirst("span.b-list-icon-link__text.ui-text.ui-text_body-1]")!!.text().trim(),
            link = serviceElement.attr("href"),
        )

        results += service
    }

    return if (results.isNotEmpty()) {
        Pair("SERVICES", results)
    } else null
}