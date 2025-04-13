package com.ae.search.model

sealed interface ISearchItem {
    val title: String
    val subtitle: String?
    val category: SearchItemCategory
    val link: String?
    val imageUri: String?
}