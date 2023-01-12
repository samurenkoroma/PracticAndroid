package com.tiun.gpstracker.domain

import org.osmdroid.util.GeoPoint

data class Location(
    val velocity: Float = 0.0f,
    val distance: Float = 0.0f,
    val geoPointsList: ArrayList<GeoPoint>
)
