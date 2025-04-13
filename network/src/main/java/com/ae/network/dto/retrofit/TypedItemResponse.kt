package com.ae.network.dto.retrofit

import com.ae.network.model.SearchItemCategory

data class TypedItemResponse(
    val title: String,
    val subtitle: String? = null,
    val category: String,
    val image: String? = null,
    val link: String? = null
)