package com.example.weatherapp.map.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import java.io.IOException


class MapFragment : Fragment()  {

    private lateinit var mapView: MapView
    private var pinMarker: Marker? = null
    lateinit var binding : FragmentMapBinding
    private var currentLocationMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false)

//        binding.mapView.onCreate(savedInstanceState)
//        binding.mapView.getMapAsync(this)

        Configuration.getInstance().load(context, requireActivity().getSharedPreferences("osmdroid", 0))


       binding.mapView.setTileSource(TileSourceFactory.MAPNIK) // Set the tile source
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapView.overlays.add(TapOverlay())
       // addPinToCurrentLocation()


        return binding.root
    }

//    private fun addPinToCurrentLocation() {
//        val currentLocation = getCurrentLocationCoordinates()
//
//
//        currentLocation?.let {
//            val startPoint = GeoPoint(it.latitude, it.longitude)
//            currentLocationMarker = Marker(binding.mapView)
//            currentLocationMarker?.position = startPoint
//            currentLocationMarker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//            binding.mapView.overlays.add(currentLocationMarker)
//            binding.mapView.controller.setCenter(startPoint)
//        }
//    }
//    private fun getCurrentLocationCoordinates(): LatLng? {
//
//        return LatLng(37.42,-122.08)
//    }

//    override fun onMapReady(p0: GoogleMap) {
//        val currentLocation = LatLng(37.42,-122.08 )
//        p0.addMarker(MarkerOptions().position(currentLocation).title("Current Location"))
//        p0.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
//
//    }

    companion object {
           @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

//    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
//       binding.mapView.overlays.remove(pinMarker)
//
//        // Add a new pin marker at the tapped location
//        p?.let {
//            pinMarker = Marker(binding.mapView)
//            pinMarker?.position = it
//            binding.mapView.overlays.add(pinMarker)
//            binding.mapView.invalidate() // Refresh the map
//        }
//
//        return true
//    }
//
//    override fun longPressHelper(p: GeoPoint?): Boolean {
//        return false
//    }

    private inner class TapOverlay : Overlay() {
        override fun onSingleTapConfirmed(e: MotionEvent, mapView: MapView): Boolean {
            // Convert tap coordinates to GeoPoint
            val p = mapView.projection.fromPixels(e.x.toInt(), e.y.toInt())

            // Remove existing pin marker
            binding.mapView.overlays.remove(pinMarker)

            // Add a new pin marker at the tapped location
            pinMarker = Marker(mapView)
            pinMarker?.position = p as GeoPoint?
            binding.mapView.overlays.add(pinMarker)
            binding.mapView.invalidate() // Refresh the map

            binding.addPinButton.setOnClickListener {
                reverseGeocode(p.latitude,p.longitude)


            }

            return true
        }
    }
    private fun reverseGeocode(latitude: Double, longitude: Double) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://nominatim.openstreetmap.org/reverse?format=json&lat=$latitude&lon=$longitude")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseBody = response.body()?.string()
                    // Parse JSON response and extract location name
                    val locationName = parseLocationName(responseBody)

                    // Update UI with location name
                    activity?.runOnUiThread {
                        // Update UI with location name (e.g., display in a TextView)
                        // textViewLocationName.text = locationName
                    }
                }
            }
        })
    }

    private fun parseLocationName(responseBody: String?): String? {
        // Parse JSON response and extract location name
        // Implement your logic to parse the JSON response and extract the location name
        // For simplicity, I'm returning a placeholder value here
        Log.i("TAG", "parseLocationName: $responseBody")
        return responseBody
    }

}