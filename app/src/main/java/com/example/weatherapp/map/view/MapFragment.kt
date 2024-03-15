package com.example.weatherapp.map.view


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
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
import java.util.Locale


class MapFragment : Fragment()  {

    private lateinit var mapView: MapView
    private var pinMarker: Marker? = null
    lateinit var binding : FragmentMapBinding
    private var currentLocationMarker: Marker? = null
    lateinit var geocoder: Geocoder
    var locationRequestID = 5
    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback


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

        geocoder = Geocoder(requireContext(), Locale.getDefault())

        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), locationRequestID
            )

        }
        getFreshLocation()

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        } else {
            getFreshLocation()
        }
    }

    @SuppressLint("MissingPermission")
    fun getFreshLocation() {
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val locationRequest: LocationRequest = LocationRequest.Builder(1000).apply {
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        }.build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val lastLocation = locationResult.lastLocation
                val currentLatitude = lastLocation?.latitude
                val currentLongitude = lastLocation?.longitude

//
//                val currentAddress = currentLatitude?.let {
//                    if (currentLongitude != null) {
//                        getAddress(it, currentLongitude)
//                    } else {
//                        null
//                    }
//                }


            }
        }

        fusedClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }


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
               // reverseGeocode(p.latitude,p.longitude)
                getAddress(p.latitude,p.longitude)



            }

            return true
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        val addresses: List<Address>
        val addressStringBuilder = StringBuilder()

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)!!
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                val city = address.adminArea
                Log.i("TAG", "getAddress: $city ")
                val country= address.countryName
                Log.i("TAG", "getAddress: $country ")


                for (i in 0..address.maxAddressLineIndex) {
                    addressStringBuilder.append(address.getAddressLine(i)).append("\n")
                }
            }
        } catch (e: IOException) {
            Log.e("Geocoder", " ${e.message}")
        }

        return addressStringBuilder.toString()
    }


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

//    private fun reverseGeocode(latitude: Double, longitude: Double) {
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url("https://nominatim.openstreetmap.org/reverse?format=json&lat=$latitude&lon=$longitude")
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//                // Handle failure
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                response.use {
//                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
//
//                    val responseBody = response.body()?.string()
//                    // Parse JSON response and extract location name
//                    val locationName = parseLocationName(responseBody)
//
//                    // Update UI with location name
//                    activity?.runOnUiThread {
//                        // Update UI with location name (e.g., display in a TextView)
//                        // textViewLocationName.text = locationName
//                    }
//                }
//            }
//        })
//    }

    private fun parseLocationName(responseBody: String?): String? {
        // Parse JSON response and extract location name
        // Implement your logic to parse the JSON response and extract the location name
        // For simplicity, I'm returning a placeholder value here
        Log.i("TAG", "parseLocationName: $responseBody")
        return responseBody
    }

}