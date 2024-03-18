package com.example.weatherapp.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.map.viewmaodel.MapViewModel
import com.example.weatherapp.model.WeatherRepository

class SettingViewModelFactory (private  var _repo: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            SettingViewModel(_repo) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")

        }
    }
}