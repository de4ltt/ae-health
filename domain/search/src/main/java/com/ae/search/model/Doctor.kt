package com.ae.search.model

data class Doctor(
    override val id: Long,
    override val title: String,
    override val subtitle: String?,
    override val imageUri: String?
) : ISearchItem {
    override val category: SearchItemCategory = SearchItemCategory.DOCTOR
        private set
}