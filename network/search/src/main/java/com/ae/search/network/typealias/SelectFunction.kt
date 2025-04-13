package com.ae.search.network.`typealias`

import com.ae.network.dto.retrofit.TypedItemResponse
import org.jsoup.nodes.Document

internal typealias SelectFunction = suspend (Document) -> List<TypedItemResponse>