package com.ae.search.`typealias`

import com.ae.search.dto.retrofit.LocatedItemResponse
import com.ae.search.model.CoordinatedArea
import com.ae.search.retrofit.IMapSearchApi

internal typealias SearchFunction =
        suspend (String, CoordinatedArea, IMapSearchApi) -> List<LocatedItemResponse>