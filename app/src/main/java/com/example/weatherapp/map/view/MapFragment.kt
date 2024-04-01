package com.example.weatherapp.map.view


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.databinding.FragmentMapBinding
import com.example.weatherapp.home.view.HomeFragment
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherapp.map.viewmaodel.MapViewModel
import com.example.weatherapp.map.viewmaodel.MapViewModelFactory
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherRepositoryImp
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.WeatherRemoteDataSourceImp
import com.example.weatherapp.util.SharedPreference
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

import org.osmdroid.config.Configuration
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
    lateinit var mapViewModel: MapViewModel
    lateinit var mapViewModelFactory: MapViewModelFactory
      var lon:Double=1.0
    var lat:Double=1.0
     lateinit var favorite: Favorite
     lateinit var homeViewModel: HomeViewModel
     lateinit var homeViewModelFactory: HomeViewModelFactory
     lateinit var weatherResponse: WeatherResponse
     lateinit var location:String
     lateinit var unit:String
     lateinit var language:String



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



        Configuration.getInstance().load(context, requireActivity().getSharedPreferences("osmdroid", 0))


       binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapView.overlays.add(TapOverlay())


        mapViewModelFactory= MapViewModelFactory(WeatherRepositoryImp.getInstance(
            WeatherRemoteDataSourceImp.getInstance(),WeatherLocalDataSourceImp(requireContext())))

        mapViewModel= ViewModelProvider(this,mapViewModelFactory).get(MapViewModel::class.java)
         location= SharedPreference.getLocation(requireContext())
        unit=SharedPreference.getUnit(requireContext())
        language=Locale.getDefault().language
        Log.i("TAG", "onCreateView:location: $location ")

        homeViewModelFactory= HomeViewModelFactory(WeatherRepositoryImp.getInstance(
            WeatherRemoteDataSourceImp.getInstance(),WeatherLocalDataSourceImp(requireContext())))

        homeViewModel= ViewModelProvider(this,homeViewModelFactory).get(HomeViewModel::class.java)


        if(location=="map"){
            binding.btnAddFav.visibility = View.GONE
            binding.btnAddHome.visibility = View.VISIBLE

        }
        else {
            binding.btnAddFav.visibility = View.VISIBLE
            binding.btnAddHome.visibility = View.GONE
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnAddFav.setOnClickListener {
            mapViewModel.insertFavorite(favorite)
        }



    }


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

                val p = mapView.projection.fromPixels(e.x.toInt(), e.y.toInt())


                binding.mapView.overlays.remove(pinMarker)


                pinMarker = Marker(mapView)
                pinMarker?.position = p as GeoPoint?
                binding.mapView.overlays.add(pinMarker)
                binding.mapView.invalidate()

                lat = p.latitude
                lon = p.longitude

                //   mapViewModel.getWeather(lon,lat,"","","")

                getAddress(p.latitude, p.longitude)


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
                    val country = address.countryName
                    Log.i("TAG", "getAddress: $country ")

                    favorite = Favorite(lat, lon, city + " - " + country)
//                    weatherResponse= WeatherResponse(lat,lon,"",0, null,)
                    SharedPreference.saveLat(requireContext(),lat)
                    SharedPreference.saveLon(requireContext(),lon)

                    if (location=="map") {
                            binding.btnAddHome.setOnClickListener {
                                Log.i("TAG", "getAddress: $city ")
                            homeViewModel.getWeather(lat, lon, "", unit, language)
//
                                homeViewModel.deleteData()
                                Toast.makeText(requireContext(), "Set Location Successfully", Toast.LENGTH_LONG).show()

                                val intent= Intent(requireContext(),MainActivity::class.java)
                                startActivity(intent)
                           }
                        }
                    //  favorite=Favorite(lat,lon,City(1,city, Coord(lat,lon),country,1,1,1,1))
                    for (i in 0..address.maxAddressLineIndex) {
                        addressStringBuilder.append(address.getAddressLine(i)).append("\n")
                    }
                }
            } catch (e: IOException) {
                Log.e("Geocoder", " ${e.message}")
            }

            return addressStringBuilder.toString()
        }




}
