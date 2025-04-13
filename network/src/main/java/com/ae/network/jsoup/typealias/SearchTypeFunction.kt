package com.ae.network.jsoup.`typealias`

import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.retrofit.IMapSearchApi

internal typealias SearchTypeFunction =
        suspend (String) -> List<TypedItemResponse>