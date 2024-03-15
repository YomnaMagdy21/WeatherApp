package com.example.weatherapp.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.Hour
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val _irepo:WeatherRepository): ViewModel() {
    private var _weather: MutableLiveData<WeatherResponse> = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> = _weather

    init {
        getWeather(37.4220936,-122.083922,"","","")
    }
//

    private fun getWeather(lat:Double, lon:Double, exclude:String, lang:String, units:String){
        viewModelScope.launch(Dispatchers.IO){
            _weather.postValue(_irepo.getWeather(lat,lon,exclude,lang,units))
        }
    }
}