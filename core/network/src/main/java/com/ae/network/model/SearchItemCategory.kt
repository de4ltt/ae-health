package com.ae.network.model

import com.ae.network.jsoup.search_function.searchForDoctor
import com.ae.network.jsoup.search_function.searchForLpu
import com.ae.network.jsoup.search_function.searchForServices
import com.ae.network.jsoup.`typealias`.SearchFunction

enum class SearchItemCategory(internal val searchFunction: SearchFunction) {
    DOCTOR(::searchForDoctor),
    LPU(::searchForLpu),
    SERVICES(::searchForServices)
}