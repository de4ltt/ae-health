package com.ae.search.model.type_item

import com.ae.search.model.interfaces.ISearchItem
import com.ae.search.model.item.util.SearchItemCategory

data class ServiceTypeItem(
    override val title: String,
    override val link: String?,
): ISearchItem {
    override val category: SearchItemCategory = SearchItemCategory.ServiceType
    override val subtitle: String? = null
    override val imageUri: String? = null
}
