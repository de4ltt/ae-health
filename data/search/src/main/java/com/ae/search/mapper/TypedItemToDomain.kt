package com.ae.search.mapper

import com.ae.search.model.interfaces.ISearchItem
import com.ae.search.model.item.util.SearchItemCategory
import com.ae.search.model.TypedItemResponse

internal fun List<TypedItemResponse>.toDomain(): List<ISearchItem> =
    this.mapNotNull { it.toDomain() }

private fun TypedItemResponse.toDomain(): ISearchItem? {
    val category = SearchItemCategory.fromString(this.category)
    return category?.createItem(title, subtitle, image, link)
}