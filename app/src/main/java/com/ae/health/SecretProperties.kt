package com.ae.health

import com.ae.config.di.ISecretProperties

object SecretProperties: ISecretProperties {
    override val baseUrl: String
        get() = BuildConfig.BASE_URL
    override val findUrl: String
        get() = BuildConfig.FIND_URL
}