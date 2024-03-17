package com.example.weatherapp.favorite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentFavDetailsBinding


class FavDetailsFragment : Fragment() {

    lateinit var binding : FragmentFavDetailsBinding

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


        return binding.root
    }

    companion object {
             @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavDetailsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}