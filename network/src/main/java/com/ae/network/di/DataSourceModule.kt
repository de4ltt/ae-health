package com.ae.network.di

import com.ae.network.ISearchDataSource
import com.ae.network.data_source.SearchDataSource
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal abstract class DataSourceModule {

    @NetworkScope
    @Binds
    abstract fun bindDataSource(dataSource: SearchDataSource): ISearchDataSource

}