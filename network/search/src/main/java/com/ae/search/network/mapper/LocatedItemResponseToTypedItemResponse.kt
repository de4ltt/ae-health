package com.ae.search.network.mapper

import com.ae.search.network.dto.retrofit.LocatedItemResponse
import com.ae.search.network.dto.retrofit.TypedItemResponse

internal fun LocatedItemResponse.toTypedItem() = TypedItemResponse(
    title = this.name,
    category = "LPU"
)

internal fun List<LocatedItemResponse>.toTypedItems() = this.map { it.toTypedItem() }