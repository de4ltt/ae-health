package com.ae.search.use_case

import com.ae.search.model.ISearchItem
import com.ae.search.model.SearchParams

interface ISearchWithinRadiusUseCase {
    suspend operator fun invoke(searchParams: SearchParams): List<ISearchItem>
}