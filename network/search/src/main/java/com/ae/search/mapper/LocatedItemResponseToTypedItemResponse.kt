package com.ae.search.mapper

import com.ae.search.dto.retrofit.LocatedItemResponse
import com.ae.search.dto.retrofit.TypedItemResponse

internal fun LocatedItemResponse.toTypedItem() = TypedItemResponse(
    title = this.name,
    category = "LPU"
)

internal fun List<LocatedItemResponse>.toTypedItems() = this.map { it.toTypedItem() }