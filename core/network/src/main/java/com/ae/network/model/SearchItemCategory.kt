package com.ae.network.model

import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.jsoup.implementation.IJsoupMapApi

internal typealias SearchFunction =
        suspend (SearchParamsNetwork, List<String>, IJsoupMapApi) -> List<TypedItemResponse>

enum class SearchItemCategory(internal val searchFunction: SearchFunction) {
    DOCTOR(::searchForDoctor),
    LPU(::searchForLpu),
    SERVICES(::searchForServices)
}

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
                            category = speciality,
                            image = imageUri,
                            link = uri
                        )
                    )
                }
            }
        }
    }

    return results
}


internal suspend fun searchForLpu(
    searchParams: SearchParamsNetwork,
    orgsLinks: List<String>,
    jsoupApi: IJsoupMapApi
): List<TypedItemResponse> {

    val results = mutableListOf<TypedItemResponse>()

    orgsLinks.forEach { link ->

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

    return results
}


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