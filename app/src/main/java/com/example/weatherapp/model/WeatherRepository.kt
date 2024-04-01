package com.example.weatherapp.model


import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
     fun getWeather(lat:Double, lon:Double, exclude:String, units:String, lang:String): Flow<WeatherResponse>


    fun getHomeWeather():Flow<WeatherResponse>
    suspend fun insertHomeWeather(weatherResponse: WeatherResponse)
    suspend fun deleteHomeWeather()

    fun getFavoriteWeather():Flow<List<Favorite>>
    suspend fun insertFavoriteWeather(favorite: Favorite)
    suspend fun deleteFavoriteWeather(favorite: Favorite)

    fun getAlertWeather():Flow<List<AlertMessage>>
    suspend fun insertAlertWeather(alert: AlertMessage)
    suspend fun deleteAlertWeather(alert: AlertMessage)
}