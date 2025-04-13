package com.ae.health.viewmodel_provider

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.ae.health.AEHeathApp

internal class AppViewModelProvider(
    private val owner: ViewModelStoreOwner,
    private val applicationContext: Context
) {
    private val appComponent = (applicationContext as AEHeathApp).appComponent

    internal inline fun <reified T : ViewModel> instanceOf(): T {
        val factory = ViewModelRegister.getFactory(T::class)
        return ViewModelProvider(owner = owner, factory = factory)[T::class]
    }
}