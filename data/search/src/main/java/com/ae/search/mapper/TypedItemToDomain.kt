package com.ae.search.mapper

import com.ae.network.dto.TypedItemDTO
import com.ae.network.dto.TypedItemsDTO
import com.ae.search.model.ClinicItem
import com.ae.search.model.DoctorItem
import com.ae.search.model.ISearchItem
import com.ae.search.model.SearchItemCategory
import com.ae.search.model.ServiceItem

private val indices = SearchItemCategory.entries

fun List<TypedItemsDTO>.toDomain(): List<ISearchItem> =
    this.map { it.results }.flatten().mapNotNull { it.toDomain() }

private fun TypedItemDTO.toDomain(): ISearchItem? =
    when (indices.find { it.toString() == this.category }) {
        SearchItemCategory.DOCTOR -> DoctorItem(
            title = title,
            subtitle = subtitle,
            imageUri = image
        )

        SearchItemCategory.LPU -> ClinicItem(
            title = title,
            subtitle = subtitle,
            imageUri = image
        )

        SearchItemCategory.SERVICES -> ServiceItem(
            title = title,
            subtitle = subtitle,
            imageUri = image
        )

        null -> null
    }