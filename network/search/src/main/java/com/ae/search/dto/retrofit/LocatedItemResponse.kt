package com.ae.search.dto.retrofit

import com.google.gson.annotations.SerializedName

internal data class LocatedItemResponse(
    val name: String,
    val lat: Double,
    val lon: Double
)
