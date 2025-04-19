package com.ae.search.model

import com.ae.search.jsoup.IJsoupFindApi
import com.ae.search.jsoup.IJsoupNearbySearchApi

sealed class SearchItemCategory {
    abstract override fun toString(): String
    internal abstract val nearbySearchApi: IJsoupNearbySearchApi
    internal abstract val findApi: IJsoupFindApi

    data object Doctor : SearchItemCategory() {
        override fun toString() = "DOCTOR"
        override lateinit var nearbySearchApi: IJsoupNearbySearchApi
        override lateinit var findApi: IJsoupFindApi
    }

    data object Lpu : SearchItemCategory() {
        override fun toString() = "LPU"
        override lateinit var nearbySearchApi: IJsoupNearbySearchApi
        override lateinit var findApi: IJsoupFindApi
    }

    data object Services : SearchItemCategory() {
        override fun toString() = "SERVICES"
        override lateinit var nearbySearchApi: IJsoupNearbySearchApi
        override lateinit var findApi: IJsoupFindApi
    }

    companion object {
        val values: List<SearchItemCategory> = listOf(Doctor, Lpu, Services)
        fun fromString(name: String): SearchItemCategory? = values.find { it.toString() == name }
    }
}