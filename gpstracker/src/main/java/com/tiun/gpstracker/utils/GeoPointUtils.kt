package com.tiun.gpstracker.utils

import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline

object GeoPointUtils {

    private const val POINT_SEPARATOR = ","
    private const val POINTS_DELIMITER = "/"

    fun geoPointsToString(list: List<GeoPoint>): String {
        val sb = StringBuilder()
        list.forEach {
            sb.append("${it.latitude}${POINT_SEPARATOR}${it.longitude}${POINTS_DELIMITER}")
        }
        return sb.toString()
    }


    fun getPolyline(geoPoints: String): Polyline {
        val polyline = Polyline()
        geoPoints.split(POINTS_DELIMITER).forEach {
            if (it.isEmpty()) return@forEach

            val point = it.split(POINT_SEPARATOR)
            polyline.addPoint(GeoPoint(point[0].toDouble(), point[1].toDouble()))
        }
        return polyline
    }

}