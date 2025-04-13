package com.ae.network.jsoup.`typealias`

import com.ae.network.dto.retrofit.TypedItemResponse
import org.jsoup.nodes.Document

internal typealias SelectFunction = suspend (Document) -> List<TypedItemResponse>