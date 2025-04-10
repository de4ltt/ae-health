@file:Suppress("NO_REFLECTION_IN_CLASS_PATH")

package com.ae.search.mapper

import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.search.model.ISearchItem
import com.ae.search.model.SearchItemCategory

internal fun List<TypedItemResponse>.toDomain(): List<ISearchItem> =
    this.mapNotNull { it.toDomain() }

private fun TypedItemResponse.toDomain(): ISearchItem? {
    val category = SearchItemCategory.fromString(this.category)
    return category?.createItem(title, subtitle, image)
}