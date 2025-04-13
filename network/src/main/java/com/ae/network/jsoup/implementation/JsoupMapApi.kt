package com.ae.network.jsoup.implementation

import com.ae.network.dto.jsoup.ClinicDoctor
import com.ae.network.dto.jsoup.ClinicMainInfo
import com.ae.network.dto.jsoup.LocatedClinicLink
import com.ae.network.jsoup.IJsoupMapApi
import com.ae.network.model.ISecretProperties
import com.ae.network.request_result.NetworkRequestError
import com.ae.network.request_result.NetworkRequestResult
import com.ae.network.util.isFuzzyMatch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

internal class JsoupMapApi @Inject constructor(
    private val secretProperties: ISecretProperties,
) : IJsoupMapApi {
    override suspend fun findClinic(
        query: String,
        lat: Double,
        lon: Double
    ): NetworkRequestResult<String> {
        val uri = "${secretProperties.defaultUri}/krasnodar/find/?q=$query&filter=lpus"

        fun getDistance(lat0: Double, lon0: Double, lat1: Double, lon1: Double) =
            2 * 3956 * asin(
                sqrt(
                    StrictMath.pow(
                        sin((lat1 - lat0) / 2),
                        2.0
                    ) + cos(lat0) * cos(lat1) * StrictMath.pow(sin((lon1 - lon0) / 2), 2.0)
                )
            )

        return try {

            var chosenClinic: LocatedClinicLink? = null
            var distance: Double? = null

            val doc: Document = Jsoup.connect(uri)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()

            val clinicElements = doc.select("div.b-section-box__elem.d-flex.py-6")

            println("$doc\n\n\n\n")

            println(clinicElements)

            for (clinicElement in clinicElements) {

                val linkElement =
                    clinicElement.selectFirst("a[data-qa=\"lpu_card_heading\"]")
                val link = linkElement?.attr("href")

                val clinicDoc = Jsoup.connect("${secretProperties.defaultUri}$link").get()

                val coordinatesElement =
                    clinicDoc.selectFirst("div[data-qa=\"lpu_card_btn_addr\"]")
                val coordinatesString = coordinatesElement?.attr("data-popup-map")
                    ?.subSequence(1, coordinatesElement.attr("data-popup-map").length / 2)

                val start = coordinatesString?.indexOfFirst { it == '[' }?.plus(2) ?: continue
                val end = coordinatesString.indexOfFirst { it == ']' }.minus(1)

                println("$start $end")

                val coordinates =
                    coordinatesString.subSequence(start, end).split(',').map { it.toDouble() }
                clinicDoc
                println(coordinates)

                if (chosenClinic == null && coordinates.size == 2) {
                    chosenClinic =
                        LocatedClinicLink(
                            link = link!!,
                            lon = coordinates[0],
                            lat = coordinates[1]
                        )
                    distance = getDistance(lat, lon, coordinates[0], coordinates[1])
                    continue
                }

                if (coordinates.size == 2 && getDistance(
                        lat,
                        lon,
                        coordinates[0],
                        coordinates[1]
                    ) <= distance!!
                ) {
                    chosenClinic =
                        LocatedClinicLink(
                            link = link!!,
                            lon = coordinates[0],
                            lat = coordinates[1]
                        )
                    distance = getDistance(lat, lon, coordinates[0], coordinates[1])
                }
            }

            if (chosenClinic == null)
                throw Exception("Not found")

            NetworkRequestResult.Success(chosenClinic.link)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }
    }

    override suspend fun getClinicInfo(link: String): NetworkRequestResult<ClinicMainInfo> {
        val uri = "${secretProperties.defaultUri}$link"

        return try {

            val doc: Document = Jsoup.connect(uri)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()

            val name = doc.selectFirst("span.ui-text.ui-text_h6.ui-kit-color-text")?.text()?.trim()

            val type = doc.selectFirst("a[data-qa=\"lpu_type\"]")?.text()?.trim()

            val address = doc.selectFirst("span[data-qa=\"lpu_adress\"")?.text()?.trim()

            val imageUri = doc.selectFirst("img[data-qa=\"lpu_image_logo\"")?.attr("src")

            val clinic = ClinicMainInfo(
                name = name!!,
                type = type,
                address = address!!,
                imageUri = imageUri
            )

            NetworkRequestResult.Success(clinic)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }
    }

    override suspend fun isContainingService(
        link: String,
        serviceQuery: String
    ): NetworkRequestResult<Boolean> {

        val uri = "${secretProperties.defaultUri}${link}price"

        return try {

            val doc: Document = Jsoup.connect(uri)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()

            val services =
                doc.select("div.b-clinic-full-price__service.b-clinic-full-price__service_js_link")

            var containsQuery = false

            for (service in services) {
                containsQuery =
                    containsQuery || service.selectFirst("div.b-clinic-full-price__key")?.text()
                        ?.isFuzzyMatch(serviceQuery) == true
            }

            NetworkRequestResult.Success(containsQuery)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }
    }

    override suspend fun getClinicDoctorsByQuery(
        link: String,
        query: String
    ): NetworkRequestResult<List<ClinicDoctor>> {

        val uri = "${secretProperties.defaultUri}${link}vrachi"

        return try {

            val doc: Document = Jsoup.connect(uri)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()

            val doctorElements = doc.select("div.b-doctor-card")

            val doctors: MutableList<ClinicDoctor> = mutableListOf()

            for (doctor in doctorElements) {

                val fullName =
                    doctor.selectFirst("span.b-doctor-card__name-surname")?.text()?.trim()

                val uri = doctor.selectFirst("div.b-doctor-card__name-link")?.attr("href")

                val specialities =
                    doctor.selectFirst("div.b-doctor-card__spec")?.text()?.split(", ")

                val imageUri = doctor.selectFirst("img.b-doctor-card__photo")?.attr("src")

                val speciality = specialities?.find { it.isFuzzyMatch(query) }

                if (speciality != null)
                    doctors.add(
                        ClinicDoctor(
                            fullName = fullName!!,
                            imageUri = imageUri,
                            speciality = speciality,
                            uri = uri!!
                        )
                    )
            }

            NetworkRequestResult.Success(doctors)
        } catch (e: Throwable) {
            NetworkRequestResult.Error(NetworkRequestError.UnknownError(e.message))
        }
    }
}