package com.ae.search.network.dto.retrofit

data class TypedItemResponse(
    val title: String,
    val subtitle: String? = null,
    val category: String,
    val image: String? = null,
    val link: String? = null
)