package com.ae.network.data_source

import com.ae.network.ISearchDataSource
import com.ae.network.dto.TypedItemsDTO
import com.ae.network.model.ISecretProperties
import com.ae.network.model.NetworkRequestError
import com.ae.network.model.NetworkRequestResult
import com.ae.network.model.SearchParamsNetwork
import com.ae.network.retrofit.SearchDataApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class SearchDataSource @Inject constructor(
    private val secretProperties: ISecretProperties,
    private val ioDispatcher: CoroutineDispatcher
) : ISearchDataSource {

    private val retrofit = Retrofit.Builder()
        .baseUrl(secretProperties.searchBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchDataApi::class.java)

    override suspend fun searchWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemsDTO>> =
        withContext(ioDispatcher) {
            try {
                val response = retrofit.searchWithFilters(
                    query = searchParams.query
                )

                if (response.body() == null)
                    throw Exception("Body is null")

                if (response.isSuccessful)
                    NetworkRequestResult.Success(response.body()!!)
                else NetworkRequestResult.Error(
                    error = when (response.code()) {
                        in 400..499 -> NetworkRequestError.ClientError
                        in 500..599 -> NetworkRequestError.ServerError
                        else -> NetworkRequestError.UnknownError
                    }
                )
            } catch (e: Exception) {
                NetworkRequestResult.Error(NetworkRequestError.UnknownError)
            }
        }

    override suspend fun searchNearby(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemsDTO>> {

    }
}