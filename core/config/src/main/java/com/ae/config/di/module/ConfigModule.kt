package com.ae.config.di.module

import com.ae.config.di.ISecretProperties
import com.ae.config.di.annotation.SecretProperty
import com.ae.config.di.scope.ConfigScope
import dagger.Module
import dagger.Provides

@Module
internal class ConfigModule {

    @ConfigScope
    @Provides
    @SecretProperty("base_url")
    fun provideBaseUrl(secretProperties: ISecretProperties): String = secretProperties.baseUrl

    @ConfigScope
    @Provides
    @SecretProperty("find_url")
    fun provideFindUrl(secretProperties: ISecretProperties): String = secretProperties.findUrl

}