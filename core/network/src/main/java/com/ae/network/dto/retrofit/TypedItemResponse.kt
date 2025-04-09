package com.ae.network.dto.retrofit

data class TypedItemResponse(
    val title: String,
    val subtitle: String? = null,
    val image: String? = null,
    val link: String
)