package com.ae.config.di.annotation

import javax.inject.Named
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SecretProperty(val name: String)
