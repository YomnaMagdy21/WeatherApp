package com.example.weatherapp.model

import com.example.weatherapp.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
     fun getWeather(lat:Double, lon:Double, exclude:String, lang:String, units:String): Flow<WeatherResponse>
    suspend fun getCurrentWeather(lat:Double, lon:Double,lang:String, units:String): Flow<CurrentWeather>

    fun getHomeWeather():Flow<WeatherResponse>
    suspend fun insertHomeWeather(weatherResponse: WeatherResponse)
    suspend fun deleteHomeWeather(weatherResponse: WeatherResponse)

    fun getFavoriteWeather():Flow<List<Favorite>>
    suspend fun insertFavoriteWeather(favorite: Favorite)
    suspend fun deleteFavoriteWeather(favorite: Favorite)

    fun getAlertWeather():Flow<List<Alert>>
    suspend fun insertAlertWeather(alert: Alert)
    suspend fun deleteAlertWeather(alert: Alert)
}