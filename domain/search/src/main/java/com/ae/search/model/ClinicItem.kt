package com.ae.search.model

data class ClinicItem(
    override val title: String,
    override val subtitle: String?,
    override val imageUri: String?,
    override val link: String?
) : ISearchItem {
    override val category: SearchItemCategory = SearchItemCategory.Lpu
}