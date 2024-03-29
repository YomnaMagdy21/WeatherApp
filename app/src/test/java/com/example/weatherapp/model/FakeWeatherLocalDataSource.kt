package com.example.weatherapp.model

import androidx.test.core.app.ApplicationProvider
import com.example.weatherapp.database.WeatherDAO
import com.example.weatherapp.database.WeatherDataBase
import com.example.weatherapp.database.WeatherLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWeatherLocalDataSource:WeatherLocalDataSource {

    var homeData:MutableList<WeatherResponse> = mutableListOf()
    var favData:MutableList<Favorite> = mutableListOf()
   //  var repo: FakeWeatherRepository=FakeWeatherRepository()
   private val dao: WeatherDAO by lazy {
       val db: WeatherDataBase = WeatherDataBase.getInstance(ApplicationProvider.getApplicationContext())
       db.getWeatherDao()
   }
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
       favData.add(favorite)
    }

    override suspend fun deleteFav(favorite: Favorite) {
        favData.remove(favorite)
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