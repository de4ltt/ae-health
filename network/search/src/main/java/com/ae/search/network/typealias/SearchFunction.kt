package com.ae.search.network.`typealias`

import com.ae.search.network.dto.retrofit.LocatedItemResponse
import com.ae.search.network.model.CoordinatedArea
import com.ae.search.network.retrofit.IMapSearchApi

internal typealias SearchFunction =
        suspend (String, CoordinatedArea, IMapSearchApi) -> List<LocatedItemResponse>