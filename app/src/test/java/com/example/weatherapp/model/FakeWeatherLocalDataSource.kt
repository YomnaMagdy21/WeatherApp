package com.example.weatherapp.model

import com.example.weatherapp.database.WeatherLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWeatherLocalDataSource:WeatherLocalDataSource {

    var homeData:MutableList<WeatherResponse> = mutableListOf()


    override suspend fun insert(weatherResponse: WeatherResponse) {
        homeData.add(weatherResponse)
    }

    override suspend fun deleteAll() {
        homeData.clear()
    }

    override fun getStoredWeather(): Flow<WeatherResponse> {
        return flowOf()
    }

    override fun getStoredFavorite(): Flow<List<Favorite>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFav(favorite: Favorite) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFav(favorite: Favorite) {
        TODO("Not yet implemented")
    }

    override fun getStoredAlert(): Flow<List<AlertMessage>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(alert: AlertMessage) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: AlertMessage) {
        TODO("Not yet implemented")
    }
}