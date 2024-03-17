package com.example.weatherapp.database

import android.content.Context
import com.example.weatherapp.model.Alert
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImp (context: Context):WeatherLocalDataSource {

    private val dao:WeatherDAO by lazy {
        val db:WeatherDataBase=WeatherDataBase.getInstance(context)
        db.getWeatherDao()
    }

    override suspend fun insert(weatherResponse: WeatherResponse) {
        dao.insert(weatherResponse)
    }

    override suspend fun delete(weatherResponse: WeatherResponse) {
        dao.delete(weatherResponse)
    }

    override fun getStoredWeather(): Flow<WeatherResponse> {
        return dao.getCurrent()
    }

    override fun getStoredFavorite(): Flow<List<Favorite>> {
        return dao.getAllFavorite()
    }

    override suspend fun insertFav(favorite: Favorite) {
         dao.insertToFavorite(favorite)
    }

    override suspend fun deleteFav(favorite: Favorite) {
        dao.deleteFromFavorite(favorite)
    }

    override fun getStoredAlert(): Flow<List<Alert>> {
        return  dao.getAllAlerts()
    }

    override suspend fun insertAlert(alert: Alert) {
        dao.insertToAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
            dao.deleteFromAlert(alert)
    }
}