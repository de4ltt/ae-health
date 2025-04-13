package com.ae.config.di

import com.ae.IConfigProperties
import com.ae.config.di.annotation.SecretProperty
import com.ae.config.di.module.ConfigModule
import com.ae.config.di.scope.ConfigScope
import dagger.Component

@ConfigScope
@Component(modules = [ConfigModule::class], dependencies = [IConfigProperties::class])
interface ConfigComponent {

    @SecretProperty("base_url")
    fun baseUrl(): String

    @SecretProperty("find_url")
    fun findUrl(): String

    @Component.Builder
    interface Builder {

        fun bindSecretProperties(secretProperties: IConfigProperties): Builder

        fun build(): ConfigComponent
    }
}