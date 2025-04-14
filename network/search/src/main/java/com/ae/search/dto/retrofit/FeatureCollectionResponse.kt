package com.ae.search.dto.retrofit

import com.google.gson.annotations.SerializedName

internal data class FeatureCollectionResponse(
    @SerializedName("features") val features: List<FeatureItemResponse>
) {

    internal data class FeatureItemResponse(
        @SerializedName("properties") val properties: PropertiesDTO,
        @SerializedName("geometry") val geometry: GeometryDTO
    ) {
        internal data class PropertiesDTO(
            @SerializedName("hintContent") val hintContent: String
        )

        internal data class GeometryDTO(
            @SerializedName("coordinates") val coordinates: List<Double>
        )

        internal fun toLocatedItems() = LocatedItemResponse(
            name = properties.hintContent,
            lat = geometry.coordinates[1],
            lon = geometry.coordinates[0]
        )
    }

    fun toLocatedItems() = features.map { it.toLocatedItems() }
}