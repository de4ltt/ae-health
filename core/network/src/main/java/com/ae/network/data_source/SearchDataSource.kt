package com.ae.network.data_source

import com.ae.network.ISearchDataSource
import com.ae.network.dto.retrofit.LocatedItemResponse
import com.ae.network.dto.retrofit.TypedItemResponse
import com.ae.network.dto.retrofit.TypedItemsResponse
import com.ae.network.jsoup.IJsoupApi
import com.ae.network.model.CoordinatedArea
import com.ae.network.model.ISecretProperties
import com.ae.network.model.NetworkRequestError
import com.ae.network.model.NetworkRequestResult
import com.ae.network.model.SearchItemCategory
import com.ae.network.model.SearchParamsNetwork
import com.ae.network.retrofit.SearchDataApi
import com.ae.network.retrofit.SearchDataNearbyApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

internal class SearchDataSource @Inject constructor(
    private val secretProperties: ISecretProperties,
    private val ioDispatcher: CoroutineDispatcher,
    private val jsoupApi: IJsoupApi
) : ISearchDataSource {

    private val searchRetrofit = Retrofit.Builder()
        .baseUrl(secretProperties.apiBaseUri)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchDataApi::class.java)

    private val mapRetrofit = Retrofit.Builder()
        .baseUrl(secretProperties.mapBaseUri)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchDataNearbyApi::class.java)

    override suspend fun searchWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> =
        withContext(ioDispatcher) {
            val result = handleNetworkRequest {
                searchRetrofit.searchWithFilters(query = searchParams.query)
            }

            if (result is NetworkRequestResult.Success)
                NetworkRequestResult.Success(result.data.mapNotNull {
                    if (searchParams.itemFilters.map { it.toString() }.contains(
                            it.title.uppercase()
                        )
                    ) it else null
                })

            result as NetworkRequestResult.Error
        }

    override suspend fun searchNearbyWithFilters(searchParams: SearchParamsNetwork): NetworkRequestResult<List<TypedItemResponse>> =
        withContext(ioDispatcher) {

            val results: MutableList<TypedItemResponse> = mutableListOf()

            val coordinatedArea = CoordinatedArea(
                lat = searchParams.lat,
                lon = searchParams.lon,
                radius = searchParams.radius!!
            )

            val mapResult = handleNetworkRequest {
                mapRetrofit.searchNearby(bbox = coordinatedArea.toString())
            }

            if (mapResult is NetworkRequestResult.Success) {

                var orgsLinks = mapResult.data.toLocatedItems().mapNotNull {

                    val res = jsoupApi.findClinic(query = it.name, lat = it.lat, lon = it.lon)

                    if (res is NetworkRequestResult.Error) null
                    else (res as NetworkRequestResult.Success).data
                }

                searchParams.itemFilters.forEach { param ->
                    when (param) {
                        SearchItemCategory.DOCTOR -> {
                            orgsLinks.forEach { link ->
                                val doctorsResult = jsoupApi.getClinicDoctorsByQuery(
                                    query = searchParams.query,
                                    link = link
                                )

                                if (doctorsResult is NetworkRequestResult.Success) {
                                    doctorsResult.data.forEach { doctor ->
                                        doctor.apply {
                                            results.add(
                                                TypedItemResponse(
                                                    title = fullName,
                                                    subtitle = speciality,
                                                    category = speciality,
                                                    image = imageUri,
                                                    link = uri
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        SearchItemCategory.LPU -> {
                            orgsLinks.forEach { link ->

                                val clinicResult = jsoupApi.getClinicInfo(link)

                                if (clinicResult is NetworkRequestResult.Success)
                                    clinicResult.data.apply {
                                        results.add(
                                            TypedItemResponse(
                                                title = name,
                                                subtitle = address,
                                                category = type ?: "LPU",
                                                image = imageUri,
                                                link = link
                                            )
                                        )
                                    }
                            }
                        }

                        SearchItemCategory.SERVICES -> {

                            orgsLinks.forEach { link ->

                                val servicesRes =
                                    jsoupApi.isContainingService(link, searchParams.query)

                                if (servicesRes is NetworkRequestResult.Success && servicesRes.data) {
                                    val clinicResult = jsoupApi.getClinicInfo(link)

                                    if (clinicResult is NetworkRequestResult.Success)
                                        clinicResult.data.apply {
                                            results.add(
                                                TypedItemResponse(
                                                    title = name,
                                                    subtitle = address,
                                                    category = type ?: "LPU",
                                                    image = imageUri,
                                                    link = link
                                                )
                                            )
                                        }
                                }
                            }
                        }
                    }
                }

                NetworkRequestResult.Success(results.toList())

            } else NetworkRequestResult.Error(NetworkRequestError.UnknownError())

        }
}