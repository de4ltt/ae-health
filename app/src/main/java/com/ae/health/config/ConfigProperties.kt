package com.ae.health.config

import com.ae.IConfigProperties
import com.ae.health.BuildConfig

object ConfigProperties: IConfigProperties {
    override val baseUrl: String
        get() = BuildConfig.BASE_URL
    override val findUrl: String
        get() = BuildConfig.FIND_URL
}