package com.tiun.gpstracker.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object TimeUtils {

    private val timeFormatter = SimpleDateFormat("HH:mm:ss:SSS")
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm")

    fun getTime(timeInMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        timeFormatter.timeZone = TimeZone.getTimeZone("UTC")
        return timeFormatter.format(calendar.time)
    }

    fun getDate(): String {
        return dateFormatter.format(Calendar.getInstance().time)
    }
}