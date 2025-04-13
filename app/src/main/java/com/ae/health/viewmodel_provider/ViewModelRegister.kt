package com.ae.health.viewmodel_provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

sealed interface ViewModelRegister<T : ViewModel> {
    val factory: ViewModelProvider.Factory

    companion object {
        private val registry: MutableMap<KClass<out ViewModel>, ViewModelProvider.Factory> = mutableMapOf()

        fun <T : ViewModel> register(modelClass: KClass<T>, factory: ViewModelProvider.Factory) {
            registry[modelClass] = factory
        }

        fun <T : ViewModel> getFactory(modelClass: KClass<T>): ViewModelProvider.Factory {
            return registry[modelClass]
                ?: throw IllegalArgumentException("Factory not found for ${modelClass.simpleName}")
        }
    }
}
