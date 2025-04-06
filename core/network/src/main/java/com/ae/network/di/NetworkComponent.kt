package com.ae.network.di

import com.ae.network.ISearchDataSource
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface NetworkComponent {

    fun searchDataSource(): ISearchDataSource

}