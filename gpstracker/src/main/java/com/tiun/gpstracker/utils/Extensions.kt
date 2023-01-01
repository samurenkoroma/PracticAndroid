package com.tiun.gpstracker.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tiun.gpstracker.R

fun Fragment.openFragment(f: Fragment) {
    (activity as AppCompatActivity)
        .supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        .replace(R.id.placeholder, f)
        .commit()
}

fun AppCompatActivity.openFragment(f: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        .replace(R.id.placeholder, f)
        .commit()
}