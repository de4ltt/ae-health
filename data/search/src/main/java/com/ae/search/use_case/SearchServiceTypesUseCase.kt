package com.ae.search.use_case

import com.ae.search.model.ISearchItem
import com.ae.search.model.SearchParams
import com.ae.search.repository.ISearchRepository
import com.ae.search.repository.SearchRepository
import javax.inject.Inject

internal class SearchServiceTypesUseCase @Inject constructor(
    private val searchRepository: ISearchRepository
): ISearchServiceTypesUseCase {
    override suspend fun invoke(searchParams: SearchParams): List<ISearchItem> =
        searchRepository.searchServiceTypes(searchParams)
}