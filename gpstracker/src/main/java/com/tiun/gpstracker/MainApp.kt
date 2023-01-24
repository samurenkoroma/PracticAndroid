package com.tiun.gpstracker

import android.app.Application
import com.tiun.gpstracker.db.MainDB

class MainApp : Application() {
    val database by lazy { MainDB.getDB(this) }
}