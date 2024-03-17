package com.example.weatherapp.database

import com.example.weatherapp.model.Alert
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    suspend fun insert(weatherResponse:WeatherResponse)
    suspend fun delete(weatherResponse: WeatherResponse)
    fun getStoredWeather(): Flow<WeatherResponse>

    fun getStoredFavorite(): Flow<List<Favorite>>
    suspend fun insertFav(favorite: Favorite)
    suspend fun deleteFav(favorite: Favorite)

    fun getStoredAlert(): Flow<List<Alert>>
    suspend fun insertAlert(alert: Alert)
    suspend fun deleteAlert(alert: Alert)

}