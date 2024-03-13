package com.example.weatherapp.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    lateinit var binding : FragmentHomeBinding
    lateinit var hourAdapter:HourAdapter
    lateinit var dayAdapter:DayAdapter




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

        setUpRecyclerViewHour()
        setUpRecyclerViewDay()
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
        binding.recViewHour.apply {
            adapter = dayAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        }

    }
    companion object {
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}