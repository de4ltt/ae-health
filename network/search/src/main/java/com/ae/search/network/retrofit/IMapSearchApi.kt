package com.ae.search.network.retrofit

import com.ae.search.network.dto.retrofit.FeatureCollectionResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

internal interface IMapSearchApi {
    @POST("/ajax/map/yamap_get_json")
    suspend fun find(
        @Query("bbox") bbox: String
    ): Response<FeatureCollectionResponse>

    @POST("/ajax/map/yamap_get_json")
    suspend fun findByLpuType(
        @Query("lputype") lpuType: String,
        @Query("bbox") bbox: String
    ): Response<FeatureCollectionResponse>

    @POST("/ajax/map/yamap_get_json")
    suspend fun findByService(
        @Query("service") service: String,
        @Query("bbox") bbox: String
    ): Response<FeatureCollectionResponse>

    @POST("/ajax/map/yamap_get_json")
    suspend fun findBySpeciality(
        @Query("speciality") speciality: String,
        @Query("bbox") bbox: String
    ): Response<FeatureCollectionResponse>
}