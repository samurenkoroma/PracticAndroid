package com.tiun.gpstracker.fragments

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tiun.gpstracker.MainApp
import com.tiun.gpstracker.MainViewModel
import com.tiun.gpstracker.R
import com.tiun.gpstracker.adapters.OSM
import com.tiun.gpstracker.databinding.FragmentMainBinding
import com.tiun.gpstracker.db.TrackItem
import com.tiun.gpstracker.domain.LocationModel
import com.tiun.gpstracker.domain.LocationService
import com.tiun.gpstracker.utils.*
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.Timer
import java.util.TimerTask

class MainFragment : Fragment() {
    private var locationModel: LocationModel? = null
    private var timer: Timer? = null
    private var startTime = 0L
    private var isServiceRunning = false
    private var firstStart = true
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var binding: FragmentMainBinding
    private val model: MainViewModel by activityViewModels {
        MainViewModel.ViewModelFactory((requireContext().applicationContext as MainApp).database)
    }
    private var pl: Polyline? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        OSM.settingsOSM(requireActivity() as AppCompatActivity)
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerPermissions()
        setOnClickListener()
        checkServiceState()
        updateTime()
        locationUpdates()
        registerLocationReceiver()
        model.tracks.observe(viewLifecycleOwner) {
            Log.d("MyTracks", it.toString())
        }
    }

    private fun setOnClickListener() = with(binding) {
        val listener = onClicks()
        fStartStop.setOnClickListener(listener)
    }

    private fun locationUpdates() = with(binding) {
        model.locationUpdates.observe(viewLifecycleOwner) {
            val distance = "Distance: ${String.format("%.2f", it.distance)} m"
            val velocity = "Velocity: ${String.format("%.2f", 3.6 * it.velocity)} km/h"
            val avVelocity = "Average: ${getAverageVelocity(it.distance)} km/h"
            tvDistance.text = distance
            tvSpeed.text = velocity
            tvAverageSpeed.text = avVelocity
            updatePolyline(it.geoPointsList)
            locationModel = it
        }
    }

    private fun onClicks(): OnClickListener {
        return OnClickListener {
            when (it.id) {
                R.id.fStartStop -> serviceStartStop()
            }
        }
    }

    private fun updateTime() {
        model.timeData.observe(viewLifecycleOwner) {
            binding.tvTime.text = it
        }
    }

    private fun getAverageVelocity(distance: Float): String {
        return String.format(
            "%.2f",
            3.6 * distance / ((System.currentTimeMillis() - startTime) / 1000)
        )
    }

    private fun getCurrentTrackTime(): String {
        return "Time: ${TimeUtils.getTime(System.currentTimeMillis() - startTime)}"
    }

    private fun startTimer() {
        timer?.cancel()
        timer = Timer()
        startTime = LocationService.startTime

        timer?.schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    model.timeData.value = getCurrentTrackTime()
                }
            }
        }, 1, 1)
    }

    private fun serviceStartStop() {
        if (!isServiceRunning) {
            startLocationService()
        } else {
            activity?.stopService(Intent(activity, LocationService::class.java))
            binding.fStartStop.setImageResource(R.drawable.ic_play)
            timer?.cancel()
            val track = getTrackItem()
            DialogManager.showSaveTrackDialog(
                requireContext(),
                track,
                object : DialogManager.Command {
                    override fun run() {
                        showToast("save track")
                        model.insertTrack(track)
                    }

                })
        }
        isServiceRunning = !isServiceRunning
    }

    fun getTrackItem(): TrackItem {
        return TrackItem(
            null,
            getCurrentTrackTime(),
            TimeUtils.getDate(),
            String.format("%.1f", locationModel?.distance?.div(1000) ?: 0),
            getAverageVelocity(locationModel?.distance ?: 0.0f),
            GeoPointUtils.geoPointsToString(locationModel?.geoPointsList ?: listOf())
        )
    }

    private fun checkServiceState() {
        isServiceRunning = LocationService.isRunning
        if (isServiceRunning) {
            binding.fStartStop.setImageResource(R.drawable.ic_stop)
            startTimer()
        }
    }

    private fun startLocationService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity?.startForegroundService(Intent(activity, LocationService::class.java))
        } else {
            activity?.startService(Intent(activity, LocationService::class.java))
        }
        binding.fStartStop.setImageResource(R.drawable.ic_stop)
        LocationService.startTime = System.currentTimeMillis()
        startTimer()
    }

    override fun onResume() {
        super.onResume()
        checkLocPermission()
    }

//    private fun settingsOSM() {
//        Configuration.getInstance().load(
//            activity as AppCompatActivity,
//            activity?.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
//        )
//        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
//    }

    private fun initOSM() = with(binding) {
        pl = Polyline()
        pl?.outlinePaint?.color = Color.GREEN
        pl?.outlinePaint?.strokeWidth = 100f
        map.controller.setZoom(20.0)
//        map.controller.animateTo(GeoPoint(50.480063388475806, 30.42303872693609))
        val mLocProvider = GpsMyLocationProvider(activity)
        val mLocOverlay = MyLocationNewOverlay(mLocProvider, map)
        mLocOverlay.enableMyLocation()
        mLocOverlay.enableFollowLocation()
        mLocOverlay.runOnFirstFix {
            map.overlays.clear()
            map.overlays.add(mLocOverlay)
            map.overlays.add(pl)
        }
    }

    private fun registerPermissions() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            when {
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) -> {
                    initOSM()
                    checkLocationEnabled()
                }

                permissions.getOrDefault(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    false
                ) -> {
                    initOSM()
                    checkLocationEnabled()
                }
                else -> showToast("not permission")
            }
        }
    }

    private fun checkLocPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkPermissionGreat10()
        } else {
            checkPermissionLess10()
        }
    }

    private fun checkPermissionLess10() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            initOSM()
            checkLocationEnabled()
        } else {
            pLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    private fun checkPermissionGreat10() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            && checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            initOSM()
            checkLocationEnabled()
        } else {
            pLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun checkLocationEnabled() {
        val lManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isEnabled = lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isEnabled) {
            DialogManager.showLocationEnableDialog(
                activity as AppCompatActivity,
                object : DialogManager.Command {
                    override fun run() {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                }
            )
        } else {
            showToast("GPS enabled")
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LocationService.LOCATION_MODEL_INTENT) {
                val locModel = intent.getSerializableExtra(
                    LocationService.LOCATION_MODEL_INTENT
                ) as LocationModel
                model.locationUpdates.value = locModel
            }

        }
    }

    private fun registerLocationReceiver() {
        LocalBroadcastManager.getInstance(activity as AppCompatActivity)
            .registerReceiver(
                receiver,
                IntentFilter(LocationService.LOCATION_MODEL_INTENT)
            )
    }

    override fun onDetach() {
        super.onDetach()
        LocalBroadcastManager.getInstance(activity as AppCompatActivity)
            .unregisterReceiver(receiver)
    }

    private fun addPoint(list: List<GeoPoint>) {
        pl?.addPoint(list[list.size - 1])
    }

    private fun fillPolyline(list: List<GeoPoint>) {
        list.forEach { pl?.addPoint(it) }
    }

    private fun updatePolyline(list: List<GeoPoint>) {
        if (list.size > 1 && firstStart) {
            fillPolyline(list)
            firstStart = false
        } else {
            addPoint(list)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}