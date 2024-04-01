package com.example.weatherapp.model

import com.example.weatherapp.CurrentWeather
import com.example.weatherapp.database.WeatherLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeWeatherRepository: WeatherRepository {

    private val weatherData = listOf<WeatherResponse>()
    var homeData:MutableList<WeatherResponse> = mutableListOf()
     var fakeWeatherLocalDataSource=FakeWeatherLocalDataSource()
    var fakeWeatherRemoteDataSource=FakeRemoteDataSource()

    private val favoriteList = mutableListOf<Favorite>()

    private val alertList = mutableListOf<AlertMessage>()

    override fun getWeather(lat: Double, lon: Double, exclude: String, units: String, lang: String): Flow<WeatherResponse> {

        return flowOf(WeatherResponse())
    }



    override fun getHomeWeather(): Flow<WeatherResponse> {
        return flowOf(WeatherResponse())
    }

    override suspend fun insertHomeWeather(weatherResponse: WeatherResponse) {
        fakeWeatherLocalDataSource.insert(weatherResponse)
    }

    override suspend fun deleteHomeWeather() {
        fakeWeatherLocalDataSource.deleteAll()
    }



    override suspend fun insertFavoriteWeather(favorite: Favorite) {
        favoriteList.add(favorite)
    }

    override suspend fun deleteFavoriteWeather(favorite: Favorite) {
        fakeWeatherLocalDataSource.deleteFav(favorite)
    }
    override fun getFavoriteWeather(): Flow<List<Favorite>> {
        return flowOf(favoriteList)
    }
    override fun getAlertWeather(): Flow<List<AlertMessage>> {
       return flowOf(alertList)
    }

    override suspend fun insertAlertWeather(alert: AlertMessage) {
       alertList.add(alert)
    }

    override suspend fun deleteAlertWeather(alert: AlertMessage) {
        alertList.remove(alert)
    }
}