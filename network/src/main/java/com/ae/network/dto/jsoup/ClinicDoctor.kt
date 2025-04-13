package com.ae.network.dto.jsoup

internal data class ClinicDoctor(
    val fullName: String,
    val imageUri: String? = null,
    val speciality: String,
    val uri: String
)
