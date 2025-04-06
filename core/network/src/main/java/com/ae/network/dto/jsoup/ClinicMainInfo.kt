package com.ae.network.dto.jsoup

internal data class ClinicMainInfo(
    val name: String,
    val type: String? = null,
    val address: String,
    val imageUri: String? = null
)
