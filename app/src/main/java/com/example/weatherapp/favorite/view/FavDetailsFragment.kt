package com.example.weatherapp.favorite.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentFavDetailsBinding
import com.example.weatherapp.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapp.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapp.home.view.DayAdapter
import com.example.weatherapp.home.view.HourAdapter
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherRepositoryImp
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.WeatherRemoteDataSourceImp
import com.example.weatherapp.util.NetworkConnection
import com.example.weatherapp.util.UIState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class FavDetailsFragment : Fragment() {

    private val TAG="TAG"

    lateinit var binding : FragmentFavDetailsBinding
    lateinit var hourAdapter: HourAdapter
    lateinit var dayAdapter: DayAdapter
    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory


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
        binding = FragmentFavDetailsBinding.inflate(inflater, container, false)
        favoriteViewModelFactory= FavoriteViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(), WeatherLocalDataSourceImp(requireContext())
            ))

        favoriteViewModel= ViewModelProvider(this,favoriteViewModelFactory).get(FavoriteViewModel::class.java)

        val favorite = arguments?.getSerializable("favorite") as? Favorite

        Log.i(TAG, "onCreateView: ${favorite?.city}")
        val currentLanguageCode = Locale.getDefault().language

        if (favorite != null) {
            favoriteViewModel.getWeather(favorite.lat,favorite.lon,"","",currentLanguageCode)
            Log.i(TAG, "onCreateView: ${favorite.city}")
        }
        else{
            Log.i(TAG, "onCreateView: nullllllllll")

        }
        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp(requireContext())
            )
        )

        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)



//
//        if (currentLanguageCode == "ar") {
//            favorite?.lat?.let { homeViewModel.getWeather(it, favorite.lon, "", "", currentLanguageCode) }
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerViewDay()
        setUpRecyclerViewHour()
        if (NetworkConnection.checkNetworkConnection(requireContext())) {
//            homeViewModel.deleteData()
            lifecycleScope.launch {
                favoriteViewModel.favorites.collectLatest { result ->
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

                            setUI(dataList)
//                            dataList?.let { weatherResponse ->
//                                homeViewModel.insertData(weatherResponse)
//                                Log.i("TAG", "onViewCreated: Inserteddddd")
//                            }
                            var latt=dataList?.lat
                            if(dataList!=null) {
                                homeViewModel.insertData(dataList)
                                Log.i(TAG, "onViewCreated:lat $latt ")

                            }

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
        }else{

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

 private   fun setUI(dataList: WeatherResponse?){
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


    companion object {
             @JvmStatic
        fun newInstance(favorite: Favorite) =
            FavDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("favorite", favorite)

                }
            }
    }
}