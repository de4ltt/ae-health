package com.ae.health.viewmodel_provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

interface ViewModelRegister<T : ViewModel> {
    val factory: ViewModelProvider.Factory

    companion object {
        private val registry: MutableMap<KClass<out ViewModel>, ViewModelProvider.Factory> = mutableMapOf()

        internal inline fun <reified T : ViewModel> register(factory: ViewModelProvider.Factory) {
            registry[T::class] = factory
        }

        fun <T : ViewModel> getFactory(modelClass: KClass<T>): ViewModelProvider.Factory {
            return registry[modelClass]
                ?: throw IllegalArgumentException("Factory not found for ${modelClass.simpleName}")
        }
    }
}
