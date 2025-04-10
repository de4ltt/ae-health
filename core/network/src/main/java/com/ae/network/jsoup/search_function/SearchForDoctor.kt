package com.ae.network.jsoup.search_function

import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.jsoup.IJsoupMapApi
import com.ae.network.model.SearchParamsNetwork
import com.ae.network.request_result.NetworkRequestResult

internal suspend fun searchForDoctor(
    searchParams: SearchParamsNetwork,
    orgsLinks: List<String>,
    jsoupApi: IJsoupMapApi
): List<TypedItemResponse> {

    val results = mutableListOf<TypedItemResponse>()

    orgsLinks.forEach { link ->

        val doctorsResult = jsoupApi.getClinicDoctorsByQuery(
            query = searchParams.query,
            link = link
        )

        if (doctorsResult is NetworkRequestResult.Success) {
            doctorsResult.data.forEach { doctor ->
                doctor.apply {
                    results.add(
                        TypedItemResponse(
                            title = fullName,
                            subtitle = speciality,
                            image = imageUri,
                            category = "DOCTOR",
                            link = uri
                        )
                    )
                }
            }
        }
    }

    return results
}