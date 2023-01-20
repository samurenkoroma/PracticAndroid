package com.tiun.gpstracker.domain

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.tiun.gpstracker.MainActivity
import com.tiun.gpstracker.R
import org.osmdroid.util.GeoPoint

class LocationService : Service() {
    private var distance = 0.0f
    private var lastLocation: Location? = null
    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var geoPoints: ArrayList<GeoPoint>

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startNotification()
        isRunning = true
        startLocationUpdates()
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        geoPoints = ArrayList()
        initLocationProvider()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        locationProvider.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(lResult: LocationResult) {
            super.onLocationResult(lResult)
            val currentLocation = lResult.lastLocation
            if (lastLocation != null && currentLocation != null) {
                distance += lastLocation?.distanceTo(currentLocation) ?: 0.0f
                geoPoints.add(GeoPoint(currentLocation))
                val locModel = LocationModel(
                    currentLocation.speed,
                    distance,
                    geoPoints
                )
                sendData(locModel)
            }
            lastLocation = currentLocation
        }
    }

    private fun sendData(locationModel: LocationModel) {
        val intent = Intent(LOCATION_MODEL_INTENT)
        intent.putExtra(LOCATION_MODEL_INTENT, locationModel)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    private fun startNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nChannel = NotificationChannel(
                CHANNEL_ID, "Location Service", NotificationManager.IMPORTANCE_DEFAULT
            )

            val nManager = getSystemService(NotificationManager::class.java) as NotificationManager
            nManager.createNotificationChannel(nChannel)
        }
        val nIntent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(
            this, 10, nIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(
            this, CHANNEL_ID
        ).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("tracker running")
            .setContentIntent(pIntent).build()
        startForeground(10, notification)
    }

    private fun initLocationProvider() {
        locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setMinUpdateIntervalMillis(1000)
            .build()
        locationProvider = LocationServices.getFusedLocationProviderClient(baseContext)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        locationProvider.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()

        )
    }

    companion object {
        const val LOCATION_MODEL_INTENT = "loc_intent"
        const val CHANNEL_ID = "channel_1"
        var isRunning = false
        var startTime = 0L
    }
}