package com.ae.search.network.`typealias`

import com.ae.search.network.dto.retrofit.TypedItemResponse

internal typealias SearchTypeFunction =
        suspend (String) -> List<TypedItemResponse>