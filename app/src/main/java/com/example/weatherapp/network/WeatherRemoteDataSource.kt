package com.example.weatherapp.network

import com.example.weatherapp.model.WeatherResponse

interface WeatherRemoteDataSource {

    suspend fun getTempOverNetwork(lat:Double,lon:Double,exclude:String,units:String,
                                   lang:String):WeatherResponse
}