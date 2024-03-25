package com.example.weatherapp.model

import com.example.weatherapp.CurrentWeather
import com.example.weatherapp.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteDataSource:WeatherRemoteDataSource {
    override fun getTempOverNetwork(
        lat: Double,
        lon: Double,
        exclude: String,
        units: String,
        lang: String
    ): Flow<WeatherResponse> {
       return flow { WeatherResponse() }
    }

    override suspend fun getCurrentWeatherOverNetwork(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): Flow<CurrentWeather> {
        TODO("Not yet implemented")
    }
}