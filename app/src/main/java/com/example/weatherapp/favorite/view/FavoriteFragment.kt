package com.example.weatherapp.favorite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentFavoriteBinding
import com.example.weatherapp.map.view.MapFragment

class FavoriteFragment : Fragment() {
    lateinit var binding : FragmentFavoriteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        binding.floatingActionButton.setOnClickListener{
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.main, MapFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }



    companion object {
         @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {

            }
    }
}