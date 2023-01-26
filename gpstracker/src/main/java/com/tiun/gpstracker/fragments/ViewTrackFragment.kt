package com.tiun.gpstracker.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.activityViewModels
import com.tiun.gpstracker.MainApp
import com.tiun.gpstracker.MainViewModel
import com.tiun.gpstracker.R
import com.tiun.gpstracker.adapters.OSM
import com.tiun.gpstracker.databinding.FragmentViewTrackBinding
import com.tiun.gpstracker.utils.GeoPointUtils
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class ViewTrackFragment : Fragment() {

    private lateinit var binding: FragmentViewTrackBinding
    private val model: MainViewModel by activityViewModels {
        MainViewModel.ViewModelFactory((requireContext().applicationContext as MainApp).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        OSM.settingsOSM(requireActivity() as AppCompatActivity)
        binding = FragmentViewTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTrack()
    }

    private fun getTrack() = with(binding) {
        model.currentTrack.observe(viewLifecycleOwner) {
            val distance = "Distance: ${it.distance} m"
            tvDistance.text = distance

            val polyline = GeoPointUtils.getPolyline(requireContext(), it.GeoPoints)
            map.overlays.add(polyline)
            setMarkers(polyline.actualPoints)
            goToStartPosition(polyline.actualPoints[0])
        }
    }

    private fun goToStartPosition(startPosition: GeoPoint) {
        binding.map.controller.apply {
            zoomTo(16.0)
            animateTo(startPosition)
        }
    }

    private fun setMarkers(list: List<GeoPoint>) = with(binding) {
        val startMarker = Marker(map)
        val finishMarker = Marker(map)
        startMarker.icon = getDrawable(requireContext(), R.drawable.ic_location_start)
        finishMarker.icon = getDrawable(requireContext(), R.drawable.ic_location_finish)
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        finishMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        startMarker.position = list[0]
        finishMarker.position = list[list.size - 1]
        map.overlays.apply {
            add(startMarker)
            add(finishMarker)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ViewTrackFragment()
    }
}