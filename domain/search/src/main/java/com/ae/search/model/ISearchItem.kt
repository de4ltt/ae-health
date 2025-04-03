package com.ae.search.model

sealed interface ISearchItem {
    val id: Long
    val title: String
    val subtitle: String?
    val category: SearchItemCategory
    val imageUri: String?
}