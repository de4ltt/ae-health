package com.ae.network.di.module

import com.ae.network.ISearchDataSource
import com.ae.network.data_source.SearchDataSource
import com.ae.network.di.scope.NetworkScope
import dagger.Binds
import dagger.Module

@Module
internal abstract class DataSourceModule {

    @NetworkScope
    @Binds
    abstract fun bindDataSource(dataSource: SearchDataSource): ISearchDataSource

}