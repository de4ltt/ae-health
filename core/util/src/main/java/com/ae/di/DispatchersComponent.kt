package com.ae.di

import com.ae.annotations.DefaultDispatcher
import com.ae.annotations.IoDispatcher
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@DispatchersScope
@Component(modules = [DispatchersModule::class])
abstract class DispatchersComponent {

    @IoDispatcher
    abstract fun ioDispatcher(): CoroutineDispatcher

    @DefaultDispatcher
    abstract fun defaultDispatcher(): CoroutineDispatcher

    @Component.Builder
    interface Builder {
        fun build(): DispatchersComponent
    }
}