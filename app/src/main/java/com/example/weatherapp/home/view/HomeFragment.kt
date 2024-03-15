package com.example.weatherapp.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.home.viewmodel.HomeViewModelFactory
import com.example.weatherapp.model.WeatherRepositoryImp
import com.example.weatherapp.network.WeatherRemoteDataSourceImp


class HomeFragment : Fragment() {

    lateinit var binding : FragmentHomeBinding
    lateinit var hourAdapter:HourAdapter
    lateinit var dayAdapter:DayAdapter
    lateinit var homeViewModel:HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory




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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModelFactory= HomeViewModelFactory(WeatherRepositoryImp.getInstance(
            WeatherRemoteDataSourceImp.getInstance(), WeatherLocalDataSourceImp(requireContext())
        ))

        homeViewModel= ViewModelProvider(this,homeViewModelFactory).get(HomeViewModel::class.java)


        setUpRecyclerViewHour()
        setUpRecyclerViewDay()
        homeViewModel.weather.observe(requireActivity()){ days ->
            dayAdapter.submitList(days.list)
        }


    }


    fun setUpRecyclerViewHour(){
        hourAdapter=HourAdapter(requireActivity())
        binding.recViewHour.apply {
            adapter = hourAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        }

    }

    fun setUpRecyclerViewDay(){
        dayAdapter=DayAdapter(requireActivity())
        binding.recViewDay.apply {
            adapter = dayAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        }

    }

    fun getCurrent(){

    }
    companion object {
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}