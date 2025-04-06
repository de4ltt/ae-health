package com.ae.network.jsoup

import com.ae.network.dto.jsoup.ClinicDoctor
import com.ae.network.dto.jsoup.ClinicMainInfo
import com.ae.network.dto.jsoup.ClinicService
import com.ae.network.dto.jsoup.LocatedClinicLink
import com.ae.network.model.NetworkRequestResult

internal interface IJsoupApi {
    suspend fun findClinic(query: String, lat: Double, lon: Double): NetworkRequestResult<String>
    suspend fun getClinicInfo(link: String): NetworkRequestResult<ClinicMainInfo>
    suspend fun isContainingService(link: String, service: String): NetworkRequestResult<Boolean>
    suspend fun getClinicDoctorsByQuery(link: String, query: String): NetworkRequestResult<List<ClinicDoctor>>
}