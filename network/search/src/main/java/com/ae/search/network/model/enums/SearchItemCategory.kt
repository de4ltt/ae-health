package com.ae.search.network.model.enums

import com.ae.search.network.search_function.search.searchForDoctor
import com.ae.network.search_function.searchForLpu
import com.ae.search.network.search_function.search.searchForServices
import com.ae.search.network.search_function.select.selectDoctors
import com.ae.search.network.search_function.select.selectLpus
import com.ae.search.network.search_function.select.selectServices
import com.ae.search.network.search_function.type.searchDoctorTypes
import com.ae.search.network.search_function.type.searchLpuTypes
import com.ae.search.network.search_function.type.searchServiceType
import com.ae.search.network.`typealias`.SearchFunction
import com.ae.search.network.`typealias`.SearchTypeFunction
import com.ae.search.network.`typealias`.SelectFunction

enum class SearchItemCategory(
    internal val searchFunction: SearchFunction,
    internal val selectFunction: SelectFunction,
    internal val searchTypeFunction: SearchTypeFunction
) {
    DOCTOR(
        searchFunction = ::searchForDoctor,
        selectFunction = ::selectDoctors,
        searchTypeFunction = ::searchDoctorTypes
    ),
    SERVICES(
        searchFunction = ::searchForServices,
        selectFunction = ::selectServices,
        searchTypeFunction = ::searchServiceType
    ),
    LPU(
        searchFunction = ::searchForLpu,
        selectFunction = ::selectLpus,
        searchTypeFunction = ::searchLpuTypes
    )
}