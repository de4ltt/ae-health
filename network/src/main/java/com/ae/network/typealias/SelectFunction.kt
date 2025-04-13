package com.ae.network.`typealias`

import com.ae.network.dto.retrofit.TypedItemResponse
import org.jsoup.nodes.Document

internal typealias SelectFunction = suspend (Document) -> List<TypedItemResponse>