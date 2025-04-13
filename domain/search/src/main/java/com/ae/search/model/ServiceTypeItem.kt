package com.ae.search.model

data class ServiceTypeItem(
    override val title: String,
    override val link: String?,
): ISearchItem {
    override val category: SearchItemCategory = SearchItemCategory.ServiceType
    override val subtitle: String? = null
    override val imageUri: String? = null
}
