package com.ae.search.dto.retrofit

import com.ae.search.model.TypedItemResponse

internal data class TypedItemsResponse(
    val title: String,
    val results: List<TypedItemResponse>
)
