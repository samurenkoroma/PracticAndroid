package com.tiun.gpstracker.adapters

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig

object OSM {
    fun settingsOSM(activity: AppCompatActivity) {
        Configuration.getInstance().load(
            activity,
            activity.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }
}