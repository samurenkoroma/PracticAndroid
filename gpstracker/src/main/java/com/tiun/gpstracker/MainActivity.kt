package com.tiun.gpstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tiun.gpstracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onButtonClicks()
    }

    private fun onButtonClicks() {
        binding.bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.id_home -> Toast.makeText(this, "home", Toast.LENGTH_SHORT).show()
                R.id.id_tracks -> Toast.makeText(this, "tracks", Toast.LENGTH_SHORT).show()
                R.id.id_settings -> Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
}