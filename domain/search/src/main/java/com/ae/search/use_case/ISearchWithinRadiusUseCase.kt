package com.ae.search.use_case

import com.ae.network_request.NetworkRequestResult
import com.ae.search.model.ISearchItem
import com.ae.search.model.SearchParams

interface ISearchWithinRadiusUseCase {
    suspend operator fun invoke(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>>
}