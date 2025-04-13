package com.ae.search.use_case

import com.ae.network.model.NetworkRequestResult
import com.ae.search.model.interfaces.ISearchItem
import com.ae.search.model.search.SearchParams
import com.ae.search.repository.ISearchRepository
import javax.inject.Inject

internal class SearchWithinRadiusUseCase @Inject constructor(
    private val searchRepository: ISearchRepository
) : ISearchWithinRadiusUseCase {
    override suspend fun invoke(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>> =
        searchRepository.searchNearby(searchParams)
}