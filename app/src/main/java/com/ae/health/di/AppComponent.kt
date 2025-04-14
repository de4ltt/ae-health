package com.ae.health.di

import android.app.Application
import com.ae.health.di.scope.AppScope
import com.ae.home.di.HomeComponent
import com.ae.home.viewmodel.SearchViewModelFactory
import dagger.Component

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