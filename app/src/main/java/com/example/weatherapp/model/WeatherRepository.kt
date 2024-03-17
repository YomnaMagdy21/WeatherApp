package com.example.weatherapp.model

import com.example.weatherapp.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeather(lat:Double, lon:Double, exclude:String, lang:String, units:String): Flow<WeatherResponse>
    suspend fun getCurrentWeather(lat:Double, lon:Double,lang:String, units:String): Flow<CurrentWeather>
}