package com.ae.search.network.search_function.select

import com.ae.search.network.dto.retrofit.TypedItemResponse
import org.jsoup.nodes.Document

internal suspend fun selectLpus(
    doc: Document
    ): List<TypedItemResponse> {

    val lpuElements = doc.select("div.b-section-box__elem.d-flex.py-6")

    val results: MutableList<TypedItemResponse> = mutableListOf()

    for (lpuElement in lpuElements) {
        val lpu = TypedItemResponse(
            title = lpuElement.selectFirst("span[data-qa=\"lpu_card_heading_lpu_name\"]")!!.text().trim(),
            subtitle = lpuElement.selectFirst("span[data-qa=\"lpu_card_btn_addr_text\"]")!!.text().trim(),
            image = lpuElement.selectFirst("img[data-qa=\"lpu_card_logo_image\"]")!!.attr("src"),
            category = "LPU",
            link = lpuElement.selectFirst("a[data-qa=\"lpu_card_heading\"]")!!.attr("href"),
        )

        results += lpu
    }

    return results
}