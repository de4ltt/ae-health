package com.ae.network.di

import com.ae.network.model.ISecretProperties
import com.ae.network.model.SecretProperties
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class SecretPropertiesModule {

//    @Singleton
    @Provides
    fun provideSecretProperties(): ISecretProperties = SecretProperties()

}