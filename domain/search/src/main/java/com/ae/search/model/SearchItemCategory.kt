package com.ae.search.model

import com.ae.search.model.item.ClinicItem
import com.ae.search.model.item.DoctorItem
import com.ae.search.model.item.ServiceItem

sealed class SearchItemCategory {
    abstract override fun toString(): String
    abstract fun createItem(
        title: String,
        subtitle: String?,
        imageUri: String?,
        link: String?
    ): ISearchItem

    object Lpu : SearchItemCategory() {
        override fun toString(): String = "LPU"
        override fun createItem(
            title: String,
            subtitle: String?,
            imageUri: String?,
            link: String?
        ) =
            ClinicItem(title, subtitle, imageUri, link)
    }

    object Doctor : SearchItemCategory() {
        override fun toString(): String = "DOCTOR"
        override fun createItem(
            title: String,
            subtitle: String?,
            imageUri: String?,
            link: String?
        ) =
            DoctorItem(title, subtitle, imageUri, link)
    }

    object Services : SearchItemCategory() {
        override fun toString(): String = "SERVICES"
        override fun createItem(
            title: String,
            subtitle: String?,
            imageUri: String?,
            link: String?
        ) =
            ServiceItem(title, subtitle, imageUri, link)
    }

    object LpuType : SearchItemCategory() {
        override fun toString(): String = "LPU_TYPE"
        override fun createItem(
            title: String,
            subtitle: String?,
            imageUri: String?,
            link: String?
        ) =
            ClinicTypeItem(title, link)
    }

    object ServiceType : SearchItemCategory() {
        override fun toString(): String = "SERVICE_TYPE"
        override fun createItem(
            title: String,
            subtitle: String?,
            imageUri: String?,
            link: String?
        ) =
            ServiceTypeItem(title, link)
    }

    object DoctorType : SearchItemCategory() {
        override fun toString(): String = "DOCTOR_TYPE"
        override fun createItem(
            title: String,
            subtitle: String?,
            imageUri: String?,
            link: String?
        ) =
            DoctorTypeItem(title, link)
    }

    companion object {
        val categoryValues by lazy { listOf<SearchItemCategory>(Doctor, Services, Lpu) }
        val typesValues by lazy { listOf<SearchItemCategory>(DoctorType, ServiceType, LpuType) }
        val allValues by lazy { categoryValues + typesValues }
        fun fromString(name: String): SearchItemCategory? =
            allValues.find { it.toString() == name }
    }

}