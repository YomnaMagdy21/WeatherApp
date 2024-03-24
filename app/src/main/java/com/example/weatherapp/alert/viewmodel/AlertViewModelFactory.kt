package com.example.weatherapp.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.model.WeatherRepository

class AlertViewModelFactory (private  var _repo: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(_repo) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")

        }
    }
}