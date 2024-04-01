package com.example.weatherapp.model

import androidx.test.core.app.ApplicationProvider
import com.example.weatherapp.database.WeatherDAO
import com.example.weatherapp.database.WeatherDataBase
import com.example.weatherapp.database.WeatherLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWeatherLocalDataSource:WeatherLocalDataSource {

    var homeData:MutableList<WeatherResponse> = mutableListOf()
    var favData:MutableList<List<Favorite>> = mutableListOf()
    var alertData:MutableList<AlertMessage> = mutableListOf()

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
        return flowOf()
    }

    override suspend fun insertFav(favorite: Favorite) {
       favData.add(listOf(favorite))
    }

    override suspend fun deleteFav(favorite: Favorite) {
        favData.remove(listOf(favorite))
    }

    override fun getStoredAlert(): Flow<List<AlertMessage>> {
        return flowOf()
    }

    override suspend fun insertAlert(alert: AlertMessage) {
        alertData.add(alert)
    }

    override suspend fun deleteAlert(alert: AlertMessage) {
        alertData.remove(alert)
    }
}