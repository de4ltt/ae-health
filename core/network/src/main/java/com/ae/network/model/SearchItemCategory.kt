package com.ae.network.model

import com.ae.network.jsoup.search_function.searchForDoctor
import com.ae.network.jsoup.search_function.searchForLpu
import com.ae.network.jsoup.search_function.searchForServices
import com.ae.network.jsoup.search_function.selectDoctors
import com.ae.network.jsoup.search_function.selectLpus
import com.ae.network.jsoup.`typealias`.SearchFunction
import com.ae.network.jsoup.`typealias`.SelectFunction

enum class SearchItemCategory(
    internal val searchFunction: SearchFunction,
    internal val selectFunction: SelectFunction
) {
    DOCTOR(searchFunction = ::searchForDoctor, selectFunction = ::selectDoctors),
    LPU(searchFunction = ::searchForLpu, selectFunction = ::selectLpus),
    SERVICES(searchFunction = ::searchForServices, selectFunction = ::selectLpus)
}