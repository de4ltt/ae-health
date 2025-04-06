package com.ae.network.dto.retrofit

data class TypedItemResponse(
    val title: String,
    val subtitle: String?,
    val category: String,
    val image: String?,
    val link: String
)