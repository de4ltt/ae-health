package com.ae.health

import android.app.Application
import com.ae.home.di.HomeComponent
import com.ae.home.viewmodel.SearchViewModelFactory
import dagger.Component
import javax.inject.Scope
import javax.inject.Singleton

@AppScope
@Component(dependencies = [HomeComponent::class])
interface AppComponent {

    fun inject(app: Application)

    fun searchViewModelFactory(): SearchViewModelFactory

    @Component.Builder
    interface Builder {

        fun bindHomeComponent(homeComponent: HomeComponent): Builder

        fun build(): AppComponent

    }
}