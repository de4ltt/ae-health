package com.ae.search.mapper

import com.ae.search.dto.retrofit.LocatedItemResponse
import com.ae.search.model.TypedItemResponse

internal fun LocatedItemResponse.toTypedItem() = TypedItemResponse(
    title = this.name,
    category = "LPU"
)

internal fun List<LocatedItemResponse>.toTypedItems(): List<TypedItemResponse> = this.map { it.toTypedItem() }