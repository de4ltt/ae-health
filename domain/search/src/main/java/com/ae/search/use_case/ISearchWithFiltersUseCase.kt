package com.ae.search.use_case

import com.ae.network.model.NetworkRequestResult
import com.ae.search.model.interfaces.ISearchItem
import com.ae.search.model.search.SearchParams

interface ISearchWithFiltersUseCase {
    suspend operator fun invoke(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>>
}