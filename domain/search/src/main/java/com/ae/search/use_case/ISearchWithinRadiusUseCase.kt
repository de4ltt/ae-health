package com.ae.search.use_case

import com.ae.network_request.NetworkRequestResult
import com.ae.search.model.interfaces.ISearchItem
import com.ae.search.model.search.SearchParams

interface ISearchWithinRadiusUseCase {
    suspend operator fun invoke(searchParams: SearchParams): NetworkRequestResult<List<ISearchItem>>
}