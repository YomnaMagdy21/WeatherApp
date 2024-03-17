package com.example.weatherapp.network

import com.example.weatherapp.CurrentWeather
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {

    suspend fun getTempOverNetwork(lat:Double,lon:Double,exclude:String,units:String,
                                   lang:String): Flow<WeatherResponse>
    suspend fun getCurrentWeatherOverNetwork(
        lat: Double,
        lon: Double,
        units:String,
        lang:String
    ): Flow<CurrentWeather>
}