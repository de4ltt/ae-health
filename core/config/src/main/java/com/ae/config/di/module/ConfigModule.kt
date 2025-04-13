package com.ae.config.di.module

import com.ae.IConfigProperties
import com.ae.config.di.annotation.SecretProperty
import com.ae.config.di.scope.ConfigScope
import dagger.Module
import dagger.Provides

@Module
internal class ConfigModule {

    @ConfigScope
    @Provides
    @SecretProperty("base_url")
    fun provideBaseUrl(secretProperties: IConfigProperties): String = secretProperties.baseUrl

    @ConfigScope
    @Provides
    @SecretProperty("find_url")
    fun provideFindUrl(secretProperties: IConfigProperties): String = secretProperties.findUrl

}