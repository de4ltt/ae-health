package com.ae.search.model

data class Diagnostic(
    override val id: Long,
    override val title: String,
    override val subtitle: String?,
    override val imageUri: String?
) : ISearchItem {
    override val category: SearchItemCategory = SearchItemCategory.DIAGNOSTICS
        private set
}