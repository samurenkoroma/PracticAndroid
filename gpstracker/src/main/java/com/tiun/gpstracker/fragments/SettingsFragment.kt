package com.tiun.gpstracker.fragments

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tiun.gpstracker.R

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var timePref: Preference
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
        init()
    }

    private fun init() {
        timePref = findPreference("update_time_key")!!
        val changeListener = onChangeListener()
        timePref.onPreferenceChangeListener = changeListener
        initPrefs()
    }

    private fun initPrefs() {
        val pref = timePref.preferenceManager.sharedPreferences
        val title = timePref.title
        timePref.title = generateTimeTitle(
            title.toString(), pref?.getString("update_time_key", "3000").toString()
        )
    }

    private fun onChangeListener(): Preference.OnPreferenceChangeListener {
        return Preference.OnPreferenceChangeListener { pref, value ->
            val title = pref.title.toString().substringBefore(":")
            pref.title = generateTimeTitle(title, value.toString())
            true
        }
    }

    private fun generateTimeTitle(title: String, index: String): String {
        val namesArray = resources.getStringArray(R.array.loc_time_update_name)
        val valuesArray = resources.getStringArray(R.array.loc_time_update_value)
        return "$title: ${namesArray[valuesArray.indexOf(index)]}"
    }
}