package com.ae.search.use_case

import com.ae.search.model.SearchParams
import com.ae.search.repository.ISearchRepository
import javax.inject.Inject
import javax.inject.Singleton

internal class SearchWithFiltersUseCase @Inject constructor(
    private val searchRepository: ISearchRepository
) : ISearchWithFiltersUseCase {

    override suspend operator fun invoke(
        searchParams: SearchParams
    ) = if (searchParams.radius == null) searchRepository.searchWithFilters(searchParams)
    else searchRepository.searchNearby(searchParams)
}