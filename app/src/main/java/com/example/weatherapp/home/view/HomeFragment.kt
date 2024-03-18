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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.CurrentWeather
import com.example.weatherapp.R
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.databinding.LocationAlertBinding
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.home.viewmodel.HomeViewModelFactory

import com.example.weatherapp.model.WeatherRepositoryImp
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.WeatherRemoteDataSourceImp
import com.example.weatherapp.util.NetworkConnection
import com.example.weatherapp.util.UIState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.properties.Delegates


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var hourAdapter: HourAdapter
    lateinit var dayAdapter: DayAdapter
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var bindingDialog:LocationAlertBinding
    lateinit var geocoder: Geocoder
    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    var locationRequestID = 5
    private var isFailure:Boolean=true
    var isSuccess:Boolean=true
    var currentLatitude =37.4220936
    var currentLongitude =-122.083922
    lateinit var languageCode :String
    var weatherRequested = false
    private var previousLanguageCode: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

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




        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//       val languageCode = Locale.getDefault().language

        setUpRecyclerViewHour()
        setUpRecyclerViewDay()
//
//        if (!weatherRequested) { // Check if weather has not been requested yet
//            getFreshLocation() // Get fresh location first
//            weatherRequested = true // Set flag to indicate weather request has been made
//        }
        val currentLanguageCode = Locale.getDefault().language

//        if (currentLanguageCode != previousLanguageCode || !weatherRequested) {
//            // Update the previous language code
//            previousLanguageCode = currentLanguageCode
//
//            // Call getFreshLocation() with the current language code
//            getFreshLocation()
//
//            // Set the weatherRequested flag to true to indicate that weather request has been made
//            weatherRequested = true
//        }

     //   homeViewModel.getWeather(currentLatitude, currentLongitude, "", "", "ar")



        //  getFreshLocation()
       // showDialogBox()
//        homeViewModel.weather.observe(requireActivity()){ days ->
//            dayAdapter.submitList(days.list)
//        }
        if(NetworkConnection.checkNetworkConnection(requireContext())) {
            Log.i("TAG", "onViewCreated: there is internet")
            homeViewModel.deleteData()
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

                            Log.i("TAG", "onViewCreatedNetwork: $dataList?.timezone")

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



                            Log.i("TAG", "onViewCreated: $dataList")
                            //  hourAdapter.submitList(dataList?.list)
                            // dayAdapter.submitList(dataList?.list)
//                        //val current=result.data as? CurrentWeather

                                updateUI(dataList)


                               dataList?.let { weatherResponse ->
                                   homeViewModel.insertData(weatherResponse)
                                   Log.i("TAG", "onViewCreated: Inserteddddd")
                               }
                           // Log.i("TAG", "onViewCreated: Inserteddddd")

//

                        }


                        else -> {
                            binding.progBar.visibility = View.GONE
                           // if(isSuccess) {
                                Toast.makeText(
                                    requireContext(),
                                    "there is problem in server",
                                    Toast.LENGTH_LONG
                                ).show()
//                                isSuccess=false
//                            }

                        }
                    }
                }
            }
        }else {
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
                            Log.i("TAG", "onViewCreatedDatabase: $dataList?.timezone")
//                           val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//                           val dateTime =
//                               LocalDateTime.parse(dataList?.list?.get(0)?.dt_txt, formatter)
//                           val date = dateTime.toLocalDate()
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



                            Log.i("TAG", "onViewCreated: $dataList")
                            //  hourAdapter.submitList(dataList?.list)
                            // dayAdapter.submitList(dataList?.list)
//                        //val current=result.data as? CurrentWeather
                            if (dataList != null) {
                                updateUI(dataList)
                            }

//

                        }


                        else -> {
                          //  if(isFailure) {
                                binding.progBar.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    "there is problem in Network connection",
                                    Toast.LENGTH_LONG
                                ).show()
//                                isFailure=false
//                            }

                        }
                    }
                }
            }

        }


    }
    override fun onResume() {
        super.onResume()

        if (!weatherRequested) {
            getFreshLocation()
            weatherRequested = true
        }
    }
    private fun getLanguagePreference(context: Context): String {
        val languageCode = Locale.getDefault().language
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("language", languageCode) ?: languageCode
    }


    private fun updateUI(dataList: WeatherResponse?) {
        binding.city.text = dataList?.city?.name
        binding.desc.text = dataList?.list?.get(0)?.weather?.get(0)?.description
        val iconName = dataList?.list?.get(0)?.weather?.get(0)?.icon
        Glide.with(requireContext())
            .load("https://openweathermap.org/img/wn/$iconName@2x.png")
            .into(binding.icon)
        binding.temp.text = dataList?.list?.get(0)?.main?.temp.toString()
        binding.date.text = dataList?.list?.get(0)?.dt_txt
        binding.pressure.text = dataList?.list?.get(0)?.main?.pressure.toString()
        binding.humidity.text = dataList?.list?.get(0)?.main?.humidity.toString()
        binding.wind.text = dataList?.list?.get(0)?.wind?.speed.toString()
        binding.clouds.text = dataList?.list?.get(0)?.clouds?.all.toString()
    }


    private fun showDialogBox(){
        val dialog= Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        // dialog.setContentView(R.layout.alert_dialog)
        bindingDialog = LocationAlertBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bindingDialog.map.setOnClickListener {
            Toast.makeText(requireContext(),"map", Toast.LENGTH_LONG).show()
        }
        bindingDialog.gps.setOnClickListener {
            Toast.makeText(requireContext(),"gps", Toast.LENGTH_LONG).show()

        }
        bindingDialog.btnSave.setOnClickListener {
            Toast.makeText(requireContext(), "save", Toast.LENGTH_LONG).show()
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
//    @SuppressLint("MissingPermission")
//    fun getFreshLocation() {
//        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//        val locationRequest: LocationRequest = LocationRequest.Builder(100000000000).apply {
//            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
//        }.build()
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                val lastLocation = locationResult.lastLocation
//                if(lastLocation != null) {
//                     currentLatitude = lastLocation.latitude
//                     currentLongitude = lastLocation.longitude
//
////                    val languageCode = Locale.getDefault().language
//                  //  val lang=getLanguagePreference(requireContext())
//                   val languageCode=Locale.getDefault().language
//                    Log.i("TAG", "onCreateView: lang code is $languageCode ")
//
//
//                    homeViewModel.getWeather(currentLatitude, currentLongitude, "", "", languageCode)
//
//                 //   Log.i("TAG", "onViewCreated: language is $lang")
//
//                }
//
//            }
//        }
//
//        fusedClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            Looper.myLooper()
//        )
//    }
@SuppressLint("MissingPermission")
fun getFreshLocation() {
    if (::fusedClient.isInitialized && ::locationCallback.isInitialized) {
        // If fusedClient and locationCallback are already initialized,
        // remove any existing location updates to prevent duplicates
        fusedClient.removeLocationUpdates(locationCallback)
    }

    fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    val locationRequest: LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        interval = 100000000000 // Update interval in milliseconds (set to a large value)
    }

    locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation = locationResult.lastLocation
            if (lastLocation != null) {
                currentLatitude = lastLocation.latitude
                currentLongitude = lastLocation.longitude

                val currentLanguageCode = Locale.getDefault().language
                // Check if the language code has changed since the last call
                if (currentLanguageCode != previousLanguageCode) {
                    previousLanguageCode = currentLanguageCode
                    homeViewModel.getWeather(currentLatitude, currentLongitude, "", "", currentLanguageCode)
                }
            }
        }
    }

    // Request location updates
    fusedClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
}



    private  fun setUpRecyclerViewHour() {
            hourAdapter = HourAdapter(requireActivity())
            binding.recViewHour.apply {
                adapter = hourAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            }

        }

      private  fun setUpRecyclerViewDay() {
            dayAdapter = DayAdapter(requireActivity())
            binding.recViewDay.apply {
                adapter = dayAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            }

        }
    }


//        fun getCurrent() {
//
//        }
//        companion object {
//            fun newInstance(param1: String, param2: String) =
//                HomeFragment().apply {
//                    arguments = Bundle().apply {
//
//                    }
//                }
//        }
//    }
