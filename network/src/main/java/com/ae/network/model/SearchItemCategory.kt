package com.ae.network.model

import com.ae.network.search_function.searchDoctorTypes
import com.ae.network.search_function.searchForDoctor
import com.ae.network.search_function.searchForLpu
import com.ae.network.search_function.searchForServices
import com.ae.network.search_function.searchLpuTypes
import com.ae.network.search_function.searchServiceType
import com.ae.network.search_function.selectDoctors
import com.ae.network.search_function.selectLpus
import com.ae.network.`typealias`.SearchFunction
import com.ae.network.`typealias`.SearchTypeFunction
import com.ae.network.`typealias`.SelectFunction

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
        selectFunction = ::selectLpus,
        searchTypeFunction = ::searchServiceType
    ),
    LPU(
        searchFunction = ::searchForLpu,
        selectFunction = ::selectLpus,
        searchTypeFunction = ::searchLpuTypes
    )
}