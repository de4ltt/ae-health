package com.ae.home.model

enum class SearchRadius(val radius: Int? = null) {
    UNSET,
    M500(500),
    KM1(1000),
    KM2(2000)
}