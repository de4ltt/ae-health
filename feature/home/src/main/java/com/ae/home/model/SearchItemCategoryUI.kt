package com.ae.home.model

import androidx.annotation.StringRes
import com.ae.mylibrary.resources.AppStrings

sealed interface SearchItemCategoryUI {
    @get:StringRes
    val resId: Int
    val name: String

    data object Lpu: SearchItemCategoryUI {
        override val resId: Int
            get() = AppStrings.Common.clinic
        override val name: String
            get() = "LPU"
    }

    data object Services: SearchItemCategoryUI {
        override val resId: Int
            get() = AppStrings.Common.services
        override val name: String
            get() = "SERVICES"
    }

    data object Doctor: SearchItemCategoryUI {
        override val resId: Int
            get() = AppStrings.Common.doctor
        override val name: String
            get() = "DOCTOR"
    }

    companion object {
        val values by lazy { listOf(Lpu, Services, Doctor) }
        fun fromString(name: String) = values.first { it.name == name }
    }
 }