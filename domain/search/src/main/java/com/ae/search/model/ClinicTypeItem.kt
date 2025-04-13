package com.ae.search.model

data class ClinicTypeItem(
    override val title: String,
    override val link: String?,
): ISearchItem {
    override val category: SearchItemCategory = SearchItemCategory.LpuType
    override val subtitle: String? = null
    override val imageUri: String? = null
}