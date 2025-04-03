package com.ae.search.model

data class Clinic(
    override val id: Long,
    override val title: String,
    override val subtitle: String?,
    override val imageUri: String?
) : ISearchItem {
    override val category: SearchItemCategory = SearchItemCategory.LPU
        private set
}