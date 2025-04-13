package com.ae.search.use_case

import com.ae.network_request.NetworkRequestResult
import com.ae.search.model.interfaces.ISearchItem
import com.ae.search.model.search.SearchParams
import com.ae.search.repository.ISearchRepository
import javax.inject.Inject
import javax.inject.Singleton

internal class SearchWithFiltersUseCase @Inject constructor(
    private val searchRepository: ISearchRepository
) : ISearchWithFiltersUseCase {

    override suspend operator fun invoke(
        searchParams: SearchParams
    ): NetworkRequestResult<List<ISearchItem>> = searchRepository.searchWithFilters(searchParams)

}