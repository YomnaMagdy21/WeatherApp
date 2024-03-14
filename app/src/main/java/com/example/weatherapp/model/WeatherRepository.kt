package com.example.weatherapp.model

interface WeatherRepository {
    suspend fun getWeather(lat:Double, lon:Double, exclude:String, lang:String, units:String):WeatherResponse
}