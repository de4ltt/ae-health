package com.ae.search.model.item

import com.ae.search.model.ISearchItem
import com.ae.search.model.SearchItemCategory

data class DoctorItem(
    override val title: String,
    override val subtitle: String?,
    override val imageUri: String?,
    override val link: String?
) : ISearchItem {
    override val category: SearchItemCategory = SearchItemCategory.Doctor
}