package com.tiun.gpstracker.domain

import org.osmdroid.util.GeoPoint

data class LocationModel(
    val velocity: Float = 0.0f,
    val distance: Float = 0.0f,
    val geoPointsList: ArrayList<GeoPoint>
) : java.io.Serializable
