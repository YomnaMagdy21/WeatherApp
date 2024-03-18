package com.example.weatherapp.home.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.example.weatherapp.util.UIState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var hourAdapter: HourAdapter
    lateinit var dayAdapter: DayAdapter
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var bindingDialog:LocationAlertBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(requireContext())
            )
        )

        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)


        setUpRecyclerViewHour()
        setUpRecyclerViewDay()
        showDialogBox()
//        homeViewModel.weather.observe(requireActivity()){ days ->
//            dayAdapter.submitList(days.list)
//        }

//        lifecycleScope.launch {
//            homeViewModel.weather
//                // Ensure only distinct values are emitted
//                .collectLatest { result ->
//                    when (result) {
//                        is UIState.Loading -> {
//                            // Handle loading state
//                        }
//                        is UIState.Success<*> -> {
//                            // Handle success state
//                        }
//                        else -> {
//                            // Handle other states
//                        }
//                    }
//                }
//        }
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
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        val dateTime = LocalDateTime.parse(dataList?.list?.get(0)?.dt_txt, formatter)
                        val date = dateTime.toLocalDate()
                        val daysToShow = mutableSetOf<String>()
                        dataList?.list?.let { weatherList ->
                            val distinctWeatherList = weatherList.filter { weatherData ->
                                val day = weatherData.dt_txt.split(" ")
                                if (daysToShow.contains(day[0])) {
                                    false
                                } else {
                                    daysToShow.add(day[0])
                                    true
                                }
                            }.take(5)

                            dayAdapter.submitList(distinctWeatherList)
                        }
                        dataList?.list?.let { weatherList ->
                            val distinctWeatherList = weatherList.filter { weatherData ->
                                val day = weatherData.dt_txt.split(" ")
                                if (daysToShow.contains(day[1])) {
                                    false
                                } else {
                                    daysToShow.add(day[1])
                                    true
                                }
                            }

                            hourAdapter.submitList(distinctWeatherList)
                        }



                            Log.i("TAG", "onViewCreated: $dataList")
                          //  hourAdapter.submitList(dataList?.list)
                       // dayAdapter.submitList(dataList?.list)
//                        //val current=result.data as? CurrentWeather
                            binding.city.text = dataList?.city?.name
                            binding.desc.text = dataList?.list?.get(0)?.weather?.get(0)?.description
                            var iconName = dataList?.list?.get(0)?.weather?.get(0)?.icon
                            Glide.with(requireContext())
                                .load("https://openweathermap.org/img/wn/$iconName@2x.png")
                                .into(binding.icon)
                            binding.temp.text = dataList?.list?.get(0)?.main?.temp.toString()

                            binding.date.text = dataList?.list?.get(0)?.dt_txt
                                //date.toString()
                            binding.pressure.text =
                                dataList?.list?.get(0)?.main?.pressure.toString()
                            binding.humidity.text =
                                dataList?.list?.get(0)?.main?.humidity.toString()
                            binding.wind.text = dataList?.list?.get(0)?.wind?.speed.toString()
                            binding.clouds.text = dataList?.list?.get(0)?.clouds?.all.toString()

//

                        }


                        else -> {
                            binding.progBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "there is problem in server",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }
                }
            }

//        lifecycleScope.launch {
//            homeViewModel.weather.distinctUntilChangedBy {
//
//
//            }
//
//
//        }
//        lifecycleScope.launch {
//            homeViewModel.weather.collectLatest { result ->
//                when (result) {
//                    is UIState.Loading -> {
//                        binding.progBar.visibility = View.VISIBLE
//
//                    }
//
//                    is UIState.Success<*> -> {
//                        binding.progBar.visibility = View.GONE
//
//                        val current=result.data as? CurrentWeather
//                        binding.city.text= current?.name
//                        binding.desc.text= current?.weather?.get(0)?.description
//                        var iconName=current?.weather?.get(0)?.icon
//                        Glide.with(requireContext()).load("https://openweathermap.org/img/wn/$iconName@2x.png").into(binding.icon)
//                        binding.temp.text= current?.main?.temp.toString()
//
//                    }
//
//                    else -> {
//                        binding.progBar.visibility = View.GONE
//                        Toast.makeText(
//                            requireContext(),
//                            "there is problem in server",
//                            Toast.LENGTH_LONG
//                        ).show()
//
//                    }
//                }
//            }
//        }


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
