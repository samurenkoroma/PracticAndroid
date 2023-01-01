package com.tiun.gpstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tiun.gpstracker.databinding.ActivityMainBinding
import com.tiun.gpstracker.fragments.MainFragment
import com.tiun.gpstracker.fragments.SettingsFragment
import com.tiun.gpstracker.fragments.TracksFragment
import com.tiun.gpstracker.utils.openFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onButtonClicks()
        openFragment(MainFragment.newInstance())
    }

    private fun onButtonClicks() {
        binding.bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.id_home -> openFragment(MainFragment.newInstance())
                R.id.id_tracks -> openFragment(TracksFragment.newInstance())
                R.id.id_settings -> openFragment(SettingsFragment.newInstance())
            }
            true
        }
    }
}