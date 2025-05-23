package com.ae.search.use_case

import com.ae.network.model.NetworkRequestResult
import com.ae.search.model.interfaces.ISearchItem
import com.ae.search.model.search.SearchParams
import com.ae.search.repository.ISearchRepository
import javax.inject.Inject

internal class SearchServiceTypesUseCase @Inject constructor(
    private val searchRepository: ISearchRepository
): ISearchServiceTypesUseCase {
    override suspend fun invoke(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>> =
        searchRepository.searchServiceTypes(searchParams)
}