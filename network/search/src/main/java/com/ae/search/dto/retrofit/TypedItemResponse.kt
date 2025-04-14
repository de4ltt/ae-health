package com.ae.search.dto.retrofit

data class TypedItemResponse(
    val title: String,
    val subtitle: String? = null,
    val category: String,
    val image: String? = null,
    val link: String? = null
)