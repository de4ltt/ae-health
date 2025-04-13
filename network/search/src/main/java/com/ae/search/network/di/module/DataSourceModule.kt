package com.ae.search.network.di.module

import com.ae.search.network.ISearchDataSource
import com.ae.search.network.data_source.SearchDataSource
import com.ae.search.network.di.scope.NetworkScope
import dagger.Binds
import dagger.Module

@Module
internal abstract class DataSourceModule {

    @NetworkScope
    @Binds
    abstract fun bindDataSource(dataSource: SearchDataSource): ISearchDataSource

}