package com.ae.network.jsoup.search_function

import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.jsoup.IJsoupMapApi
import com.ae.network.model.SearchParamsNetwork
import com.ae.network.request_result.NetworkRequestResult

internal suspend fun searchForServices(
    searchParams: SearchParamsNetwork,
    orgsLinks: List<String>,
    jsoupApi: IJsoupMapApi
): List<TypedItemResponse> {

    val results = mutableListOf<TypedItemResponse>()

    orgsLinks.forEach { link ->

        val servicesRes =
            jsoupApi.isContainingService(link, searchParams.query)

        if (servicesRes is NetworkRequestResult.Success && servicesRes.data) {
            val clinicResult = jsoupApi.getClinicInfo(link)

            if (clinicResult is NetworkRequestResult.Success)
                clinicResult.data.apply {
                    results.add(
                        TypedItemResponse(
                            title = name,
                            subtitle = address,
                            category = type ?: "LPU",
                            image = imageUri,
                            link = link
                        )
                    )
                }
        }
    }

    return results
}