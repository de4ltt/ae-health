package com.ae.search.model

data class DoctorTypeItem(
    override val title: String,
    override val link: String?,
): ISearchItem {
    override val category: SearchItemCategory = SearchItemCategory.DoctorType
    override val subtitle: String? = null
    override val imageUri: String? = null
}
