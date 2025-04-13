package com.ae.health

import com.ae.IConfigProperties

object ConfigProperties: IConfigProperties {
    override val baseUrl: String
        get() = BuildConfig.BASE_URL
    override val findUrl: String
        get() = BuildConfig.FIND_URL
}