package com.ae.network.jsoup.search_function

import com.ae.network.dto.retrofit.TypedItemResponse
import org.jsoup.nodes.Document

internal suspend fun selectDoctors(
    doc: Document
): List<TypedItemResponse> {

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

    return results
}