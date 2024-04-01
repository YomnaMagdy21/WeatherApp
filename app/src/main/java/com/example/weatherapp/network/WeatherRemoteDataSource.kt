package com.example.weatherapp.network


import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {

     fun getTempOverNetwork(lat:Double,lon:Double,exclude:String,units:String,
                                   lang:String): Flow<WeatherResponse>

}