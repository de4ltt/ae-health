package com.ae.config

@Module
internal class SecretPropertiesModule {

//    @Singleton
    @Provides
    fun provideSecretProperties(): ISecretProperties = SecretProperties()

}