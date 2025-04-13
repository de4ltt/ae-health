package com.ae.network.jsoup.`typealias`

import com.ae.network.dto.retrofit.LocatedItemResponse
import com.ae.network.model.CoordinatedArea
import com.ae.network.retrofit.IMapSearchApi

internal typealias SearchFunction =
        suspend (String, CoordinatedArea, IMapSearchApi) -> List<LocatedItemResponse>