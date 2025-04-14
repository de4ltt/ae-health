package com.ae.search.dto.retrofit

internal data class TypedItemsResponse(
    val title: String,
    val results: List<TypedItemResponse>
)
