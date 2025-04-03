package com.ae.search.use_case

import com.ae.search.model.SearchParams
import com.ae.search.repository.ISearchRepository

class SearchWithFiltersUseCase(
    private val searchRepository: ISearchRepository
) {

    operator fun invoke(
        searchParams: SearchParams
    ) = searchRepository.search(searchParams)
}