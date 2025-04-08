package com.ae.search.di

import com.ae.search.repository.ISearchRepository
import com.ae.search.repository.SearchRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal abstract class SearchModule {

    @Binds
    abstract fun bindSearchRepository(searchRepository: SearchRepository): ISearchRepository

}