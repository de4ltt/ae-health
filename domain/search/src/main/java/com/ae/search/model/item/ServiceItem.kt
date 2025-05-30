package com.ae.search.model.item

import com.ae.search.model.interfaces.ISearchItem
import com.ae.search.model.item.util.SearchItemCategory

data class ServiceItem(
    override val title: String,
    override val subtitle: String?,
    override val imageUri: String?,
    override val link: String?
) : ISearchItem {
    override val category: SearchItemCategory = SearchItemCategory.Services
}