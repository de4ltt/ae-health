package com.ae.health.config

import androidx.compose.runtime.Stable
import com.ae.IConfigProperties
import com.ae.health.BuildConfig

object ConfigProperties: IConfigProperties {

    @Stable
    override val baseUrl: String
        get() = BuildConfig.BASE_URL

    @Stable
    override val findUrl: String
        get() = BuildConfig.FIND_URL
}