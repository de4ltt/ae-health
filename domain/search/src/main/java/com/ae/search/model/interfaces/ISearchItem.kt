package com.ae.search.model.interfaces

import com.ae.search.model.item.util.SearchItemCategory

interface ISearchItem {
    val title: String
    val subtitle: String?
    val category: SearchItemCategory
    val link: String?
    val imageUri: String?
}