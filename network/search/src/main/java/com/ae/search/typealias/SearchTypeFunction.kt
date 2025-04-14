package com.ae.search.`typealias`

import com.ae.search.dto.retrofit.TypedItemResponse

internal typealias SearchTypeFunction =
        suspend (String) -> List<TypedItemResponse>