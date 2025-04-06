package com.ae.health

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [])
interface AppComponent {

    fun inject(activity: MainActivity)

    @Component.Builder
    abstract class AppComponentBuilder {

        @BindsInstance
        abstract fun bindContext(context: Context): AppComponentBuilder

        abstract fun build(): AppComponent
    }

}