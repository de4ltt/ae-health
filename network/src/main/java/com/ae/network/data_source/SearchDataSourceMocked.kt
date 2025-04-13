package com.ae.network.data_source

import com.ae.network.ISearchDataSource
import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.request_result.NetworkRequestError
import com.ae.network.request_result.NetworkRequestResult
import com.ae.network.model.SearchParamsNetwork
import javax.inject.Inject
import kotlin.random.Random

class SearchDataSourceMocked @Inject constructor() : ISearchDataSource {

    val types = listOf("DOCTOR", "LPU", "SERVICES")

    fun generateList(size: Int) = List(size = size) { it ->
        TypedItemResponse(
            title = "Title #$it",
            subtitle = "Subtitle #$it",
            category = types[it % 3],
            image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQoFRQjM-wM_nXMA03AGDXgJK3VeX7vtD3ctA&s",
            link = "procum.com"
        )
    }

    override suspend fun searchWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> {
        print("SEARCH WITH FILTERS TRIGGERED!")
        return if (Random.nextInt(10) == 0)
            NetworkRequestResult.Error(NetworkRequestError.UnknownError("Hamburger"))
        else
            NetworkRequestResult.Success(generateList(Random.nextInt(3, 20)))
    }

    override suspend fun searchNearbyWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> {
        return if (Random.nextInt(10) == 0)
            NetworkRequestResult.Error(NetworkRequestError.UnknownError("Hamburger"))
        else
            NetworkRequestResult.Success(generateList(Random.nextInt(4, 40)))
    }

    override suspend fun searchServiceTypes(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> {
        TODO("Not yet implemented")
    }
}
