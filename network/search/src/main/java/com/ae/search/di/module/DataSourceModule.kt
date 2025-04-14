package com.ae.search.di.module

import com.ae.search.ISearchDataSource
import com.ae.search.data_source.SearchDataSource
import com.ae.search.di.scope.NetworkScope
import dagger.Binds
import dagger.Module

@Module
internal abstract class DataSourceModule {

    @NetworkScope
    @Binds
    abstract fun bindDataSource(dataSource: SearchDataSource): ISearchDataSource

}