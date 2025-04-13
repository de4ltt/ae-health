package com.ae.network.mapper

import com.ae.network.dto.retrofit.LocatedItemResponse
import com.ae.network.dto.retrofit.TypedItemResponse

internal fun LocatedItemResponse.toTypedItem() = TypedItemResponse(
    title = this.name,
    category = "LPU"
)

internal fun List<LocatedItemResponse>.toTypedItems() = this.map { it.toTypedItem() }