package com.tiun.gpstracker.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tiun.gpstracker.R

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var timePref: Preference
    private lateinit var colorPref: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
        init()
    }

    private fun init() {
        timePref = findPreference("update_time_key")!!
        colorPref = findPreference("color_key")!!
        val changeListener = onChangeListener()
        timePref.onPreferenceChangeListener = changeListener
        colorPref.onPreferenceChangeListener = changeListener
        initPrefs()
    }

    private fun initPrefs() {
        val pref = timePref.preferenceManager.sharedPreferences
        val title = timePref.title
        timePref.title = generateTimeTitle(
            title.toString(), pref?.getString("update_time_key", "3000").toString()
        )

        val trackColor = pref?.getString("color_key", "#CCFF0000")
        colorPref.icon?.setTint(Color.parseColor(trackColor))
    }

    private fun onChangeListener(): Preference.OnPreferenceChangeListener {
        return Preference.OnPreferenceChangeListener { pref, value ->
            when (pref.key) {
                "update_time_key" -> onTimeChange(value.toString())
                "color_key" -> colorPref.icon?.setTint(Color.parseColor(value.toString()))
            }
            true
        }
    }

    private fun onTimeChange(value: String) {
        val title = timePref.title.toString().substringBefore(":")
        timePref.title = generateTimeTitle(title, value)
    }


    private fun generateTimeTitle(title: String, index: String): String {
        val namesArray = resources.getStringArray(R.array.loc_time_update_name)
        val valuesArray = resources.getStringArray(R.array.loc_time_update_value)
        return "$title: ${namesArray[valuesArray.indexOf(index)]}"
    }
}