package com.ae.search.model

sealed class SearchItemCategory {
    abstract override fun toString(): String
    abstract fun createItem(title: String, subtitle: String?, imageUri: String?): ISearchItem

    object Doctor : SearchItemCategory() {
        override fun toString(): String = "DOCTOR"
        override fun createItem(title: String, subtitle: String?, imageUri: String?) =
            DoctorItem(title, subtitle, imageUri)
    }

    object Lpu : SearchItemCategory() {
        override fun toString(): String = "LPU"
        override fun createItem(title: String, subtitle: String?, imageUri: String?) =
            ClinicItem(title, subtitle, imageUri)
    }

    object Services : SearchItemCategory() {
        override fun toString(): String = "SERVICES"
        override fun createItem(title: String, subtitle: String?, imageUri: String?) =
            ServiceItem(title, subtitle, imageUri)
    }

    companion object {
        val values = listOf(Doctor, Lpu, Services)
        fun fromString(name: String): SearchItemCategory? =
            values.find { it.toString() == name }
    }
}