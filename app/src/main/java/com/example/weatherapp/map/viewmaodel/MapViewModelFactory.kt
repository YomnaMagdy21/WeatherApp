package com.example.weatherapp.map.viewmaodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.WeatherRepository

class MapViewModelFactory (private  var _repo:WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapViewModel::class.java)){
            MapViewModel(_repo) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")

        }
    }
}