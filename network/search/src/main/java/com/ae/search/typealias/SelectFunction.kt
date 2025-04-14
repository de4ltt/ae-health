package com.ae.search.`typealias`

import com.ae.search.dto.retrofit.TypedItemResponse
import org.jsoup.nodes.Document

internal typealias SelectFunction = suspend (Document) -> List<TypedItemResponse>