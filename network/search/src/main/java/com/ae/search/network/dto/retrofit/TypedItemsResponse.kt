package com.ae.search.network.dto.retrofit

internal data class TypedItemsResponse(
    val title: String,
    val results: List<TypedItemResponse>
)
