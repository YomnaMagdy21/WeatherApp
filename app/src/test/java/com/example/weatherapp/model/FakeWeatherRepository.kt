package com.example.weatherapp.model

import com.example.weatherapp.CurrentWeather
import com.example.weatherapp.database.WeatherLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWeatherRepository: WeatherRepository {

    private val weatherData = listOf<WeatherResponse>()
    private var getWeatherCalledWith: Pair<Double, Double>? = null
    var homeData:MutableList<WeatherResponse> = mutableListOf()
     var fakeWeatherLocalDataSource=FakeWeatherLocalDataSource()

    override fun getWeather(lat: Double, lon: Double, exclude: String, units: String, lang: String): Flow<WeatherResponse> {
       // getWeatherCalledWith = Pair(lat, lon)
        return flowOf(WeatherResponse()) // Return the first item or customize behavior
    }

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): Flow<CurrentWeather> {
        return flowOf()
    }

    override fun getHomeWeather(): Flow<WeatherResponse> {
        return flowOf(WeatherResponse())
    }

    override suspend fun insertHomeWeather(weatherResponse: WeatherResponse) {
        fakeWeatherLocalDataSource.insert(weatherResponse)
    }

    override suspend fun deleteHomeWeather() {
        TODO("Not yet implemented")
    }

    override fun getFavoriteWeather(): Flow<List<Favorite>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFavoriteWeather(favorite: Favorite) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavoriteWeather(favorite: Favorite) {
        TODO("Not yet implemented")
    }

    override fun getAlertWeather(): Flow<List<AlertMessage>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlertWeather(alert: AlertMessage) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlertWeather(alert: AlertMessage) {
        TODO("Not yet implemented")
    }
}