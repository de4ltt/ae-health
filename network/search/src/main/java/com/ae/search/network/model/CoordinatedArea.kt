package com.ae.search.network.model

import kotlin.math.cos

internal class CoordinatedArea {
    val latStart: Double
    val lonStart: Double
    val latEnd: Double
    val lonEnd: Double

    override fun toString(): String {
        return "$latStart,$lonStart,$latEnd,$lonEnd"
    }

    constructor(lat: Double, lon: Double, radius: Int) {
        val earthRadius = 6371.0

        val latDelta = Math.toDegrees(radius / 1000.0 / earthRadius)
        val lonDelta = Math.toDegrees(radius / 1000.0 / (earthRadius * cos(Math.toRadians(lat))))

        val latStart = lat - latDelta
        val latEnd = lat + latDelta
        val lonStart = lon - lonDelta
        val lonEnd = lon + lonDelta

        this.latStart = latStart
        this.lonStart = lonStart
        this.latEnd = latEnd
        this.lonEnd = lonEnd
    }
}
