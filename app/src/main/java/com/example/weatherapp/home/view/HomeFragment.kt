package com.example.weatherapp.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.databinding.FragmentMapBinding
import com.example.weatherapp.databinding.LocationAlertBinding
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherapp.map.view.MapFragment

import com.example.weatherapp.model.WeatherRepositoryImp
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.WeatherRemoteDataSourceImp
import com.example.weatherapp.util.NetworkConnection
import com.example.weatherapp.util.SharedPreference
import com.example.weatherapp.util.UIState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var hourAdapter: HourAdapter
    lateinit var dayAdapter: DayAdapter
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var bindingDialog: LocationAlertBinding
    lateinit var geocoder: Geocoder
    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    var locationRequestID = 5
    private var isFailure: Boolean = true
    var isSuccess: Boolean = true
    var currentLatitude = 31.1964949
    var currentLongitude = 29.9273458
    lateinit var language: String
    lateinit var  unit:String
    var lat:Double=0.0
    var lon:Double=0.0
    var weatherRequested = false
    private var previousLanguageCode: String? = null
    lateinit var bindingMap: FragmentMapBinding

    lateinit var unitOfWindSpeed:String
     lateinit var location:String

    var firstTime=false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        bindingMap = FragmentMapBinding.inflate(inflater, container, false)

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

        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(requireContext())
            )
        )

        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
        unit=SharedPreference.getUnit(requireContext())
        location = SharedPreference.getLocation(requireContext())
        lat=SharedPreference.getLat(requireContext())
        lon=SharedPreference.getLon(requireContext())
        language=SharedPreference.getLanguage(requireContext())
//        homeViewModel.getWeather(
//            currentLatitude,
//            currentLongitude,
//            "",
//            unit,
//            "ar"
//        )



        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//       val languageCode = Locale.getDefault().language

        setUpRecyclerViewHour()
        setUpRecyclerViewDay()

        unit=SharedPreference.getUnit(requireContext())
         location = SharedPreference.getLocation(requireContext())
        lat=SharedPreference.getLat(requireContext())
        lon=SharedPreference.getLon(requireContext())
        language=SharedPreference.getLanguage(requireContext())


        Log.i("TAG", "onViewCreated:loc $location ")
        if (NetworkConnection.checkNetworkConnection(requireContext())) {
            homeViewModel.deleteData()
            getFreshLocation()

            if (location == "map") {
                bindingMap.btnAddFav.visibility = View.GONE
                bindingMap.btnAddHome.visibility = View.VISIBLE
                // homeViewModel.getWeather(lat,)
            } else {
                //homeViewModel.deleteData()

            }
            Log.i("TAG", "onViewCreated: there is internet")
            //   homeViewModel.deleteData()
            lifecycleScope.launch {
                homeViewModel.weather.collectLatest { result ->
                    when (result) {
                        is UIState.Loading -> {
                            binding.progBar.visibility = View.VISIBLE
                            binding.recViewHour.visibility = View.GONE
                            binding.recViewDay.visibility = View.GONE
                            binding.cardView.visibility = View.GONE
                        }

                        is UIState.Success<*> -> {
                            binding.progBar.visibility = View.GONE
                            binding.recViewHour.visibility = View.VISIBLE
                            binding.recViewDay.visibility = View.VISIBLE
                            binding.cardView.visibility = View.VISIBLE
                            //  getFreshLocation()
                            val dataList = result.data as? WeatherResponse

                            setUI(dataList)


//                               dataList?.let { weatherResponse ->
//                                   homeViewModel.insertData(weatherResponse)
//                                   Log.i("TAG", "onViewCreated: Inserteddddd")
//                               }
                            if (dataList != null) {
                                homeViewModel.insertData(dataList)
                            }
                            // Log.i("TAG", "onViewCreated: Inserteddddd")

                        }

                        else -> {
                            binding.progBar.visibility = View.GONE

                            Toast.makeText(
                                requireContext(),
                                "there is problem in server",
                                Toast.LENGTH_LONG
                            ).show()
//
//                            }

                        }
                    }
                }
            }
        } else {
            Log.i("TAG", "onViewCreated: there is no internet")

            homeViewModel.getLocalData()
            lifecycleScope.launch {
                homeViewModel.weather.collectLatest { result ->
                    when (result) {
                        is UIState.Loading -> {
                            binding.progBar.visibility = View.VISIBLE
                            binding.recViewHour.visibility = View.GONE
                            binding.recViewDay.visibility = View.GONE
                            binding.cardView.visibility = View.GONE
                        }

                        is UIState.Success<*> -> {
                            binding.progBar.visibility = View.GONE
                            binding.recViewHour.visibility = View.VISIBLE
                            binding.recViewDay.visibility = View.VISIBLE
                            binding.cardView.visibility = View.VISIBLE

                            val dataList = result.data as? WeatherResponse

                            if (dataList != null) {
                                setUI(dataList)
                            }
                        }


                        else -> {

                            binding.progBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "there is problem in Network connection",
                                Toast.LENGTH_LONG
                            ).show()
//
//                            }

                        }
                    }
                }
            }

        }



    }



    private fun getLanguagePreference(context: Context): String {
        val languageCode = Locale.getDefault().language
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("language", languageCode) ?: languageCode
    }


    private fun setUI(dataList: WeatherResponse?) {
        val daysToShow = mutableSetOf<String>()
        dataList?.list.let { weatherList ->
            val distinctWeatherList = weatherList?.filter { weatherData ->
                val day = weatherData.dt_txt.split(" ")
                if (daysToShow.contains(day[0])) {
                    false
                } else {
                    daysToShow.add(day[0])
                    true
                }
            }?.take(5)

            dayAdapter.submitList(distinctWeatherList)
        }
        dataList?.list.let { weatherList ->
            val distinctWeatherList = weatherList?.filter { weatherData ->
                val hour = weatherData.dt_txt.split(" ")
                if (daysToShow.contains(hour[1])) {
                    false
                } else {
                    daysToShow.add(hour[1])
                    true
                }
            }

            hourAdapter.submitList(distinctWeatherList)
        }


        binding.city.text = dataList?.city?.name
        binding.desc.text = dataList?.list?.get(0)?.weather?.get(0)?.description
        val iconName = dataList?.list?.get(0)?.weather?.get(0)?.icon
        Glide.with(requireContext())
            .load("https://openweathermap.org/img/wn/$iconName@2x.png")
            .into(binding.icon)
        if(unit=="metric") {
            binding.temp.text = dataList?.list?.get(0)?.main?.temp.toString()+"°C"
        }else if(unit=="imperial"){
            binding.temp.text = dataList?.list?.get(0)?.main?.temp.toString()+"°F"

        }
        else{
            binding.temp.text = dataList?.list?.get(0)?.main?.temp.toString()+"°K"

        }
        binding.date.text = dataList?.list?.get(0)?.dt_txt
        binding.pressure.text = dataList?.list?.get(0)?.main?.pressure.toString()+" hpa"
        binding.humidity.text = dataList?.list?.get(0)?.main?.humidity.toString()+" %"
        unitOfWindSpeed=SharedPreference.getWindUnit(requireContext())
        if(unitOfWindSpeed=="Imperial"){
               var milePerSec=(dataList?.list?.get(0)?.wind?.speed)
            var value= milePerSec?.times(2.237)?.let { String.format("%.2f", it) }
            binding.wind.text=value.toString()+" m/h"
        }else{
            binding.wind.text = dataList?.list?.get(0)?.wind?.speed.toString()+" m/s"

        }
        binding.clouds.text = dataList?.list?.get(0)?.clouds?.all.toString()+" %"
    }


    private fun showDialogBox() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        // dialog.setContentView(R.layout.alert_dialog)
        bindingDialog = LocationAlertBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bindingDialog.map.setOnClickListener {
            Toast.makeText(requireContext(), "map", Toast.LENGTH_LONG).show()
            SharedPreference.saveLocation(requireContext(),"map")
        }
        bindingDialog.gps.setOnClickListener {
            Toast.makeText(requireContext(), "gps", Toast.LENGTH_LONG).show()
            SharedPreference.saveLocation(requireContext(),"gps")

        }
        bindingDialog.btnSave.setOnClickListener {
            Toast.makeText(requireContext(), "save $location", Toast.LENGTH_LONG).show()

            if(location=="map"){
                val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main, MapFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
            dialog.dismiss()


        }
        dialog.show()

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
        val locationRequest: LocationRequest = LocationRequest.Builder(100000000000).apply {
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        }.build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val lastLocation = locationResult.lastLocation
                if (lastLocation != null) {
                    currentLatitude = lastLocation.latitude
                    currentLongitude = lastLocation.longitude

//                    val languageCode = Locale.getDefault().language
                    //  val lang=getLanguagePreference(requireContext())
                    val currentLanguageCode = SharedPreference.getLanguage(requireContext())
                    unit=SharedPreference.getUnit(requireContext())
                   if(location=="gps") {
                        homeViewModel.deleteData()
//                       if (currentLanguageCode == "ar") {
//                           homeViewModel.getWeather(
//                               currentLatitude,
//                               currentLongitude,
//                               "",
//                               unit,
//                               "ar"
//                           )
//                       } else {
//                           homeViewModel.getWeather(
//                               currentLatitude,
//                               currentLongitude,
//                               "",
//                               unit,
//                               "en"
//                           )
//
//                       }
                       homeViewModel.getWeather(
                           currentLatitude,
                           currentLongitude,
                           "",
                           unit,
                           currentLanguageCode
                       )

                       SharedPreference.saveLat(requireContext(),currentLatitude)
                       SharedPreference.saveLon(requireContext(),currentLongitude)
                   }else{
                       homeViewModel.deleteData()
                       Log.i("TAG", "onLocationResult:  ")
                       homeViewModel.getWeather(
                           lat,
                           lon,
                           "",
                           unit,
                           currentLanguageCode
                       )
//                       if (currentLanguageCode == "ar") {
//                           homeViewModel.getWeather(
//                               lat,
//                               lon,
//                               "",
//                               unit,
//                               "ar"
//                           )
//                       } else {
//                           homeViewModel.getWeather(
//                               lat,
//                               lon,
//                               "",
//                               unit,
//                               "en"
//                           )
//
//                       }
                       SharedPreference.saveLat(requireContext(),lat)
                       SharedPreference.saveLon(requireContext(),lon)
                   }



                }

            }
        }

        fusedClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }



    private fun setUpRecyclerViewHour() {
        hourAdapter = HourAdapter(requireActivity())
        binding.recViewHour.apply {
            adapter = hourAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        }

    }

    private fun setUpRecyclerViewDay() {
        dayAdapter = DayAdapter(requireActivity())
        binding.recViewDay.apply {
            adapter = dayAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        }

    }





    }
