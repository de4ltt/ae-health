package com.ae.search.use_case

import com.ae.search.di.SearchDataScope
import com.ae.search.model.ISearchItem
import com.ae.search.model.SearchParams
import com.ae.search.repository.ISearchRepository
import javax.inject.Inject

internal class SearchWithinRadiusUseCase @Inject constructor(
    private val searchRepository: ISearchRepository
) : ISearchWithinRadiusUseCase {
    override suspend fun invoke(searchParams: SearchParams): List<ISearchItem> =
        searchRepository.searchNearby(searchParams)
}