package com.ae.search.network.`typealias`

import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.retrofit.IMapSearchApi

internal typealias SearchTypeFunction =
        suspend (String) -> List<TypedItemResponse>