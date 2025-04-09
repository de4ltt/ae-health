package com.ae.network.jsoup.`typealias`

import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.jsoup.IJsoupMapApi
import com.ae.network.model.SearchParamsNetwork

internal typealias SearchFunction =
        suspend (SearchParamsNetwork, List<String>, IJsoupMapApi) -> List<TypedItemResponse>