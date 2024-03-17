package com.example.weatherapp.model

import com.example.weatherapp.CurrentWeather
import com.example.weatherapp.database.WeatherLocalDataSource
import com.example.weatherapp.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImp private constructor(private var weatherRemoteDataSource: WeatherRemoteDataSource,
                                               private var weatherLocalDataSource: WeatherLocalDataSource
):WeatherRepository{

    companion object{
        private var instance:WeatherRepositoryImp?=null
        fun getInstance(
            weatherRemoteDataSource: WeatherRemoteDataSource,
            weatherLocalDataSource: WeatherLocalDataSource
        ):WeatherRepositoryImp{
            return instance?: synchronized(this){
                val temp=WeatherRepositoryImp(
                    weatherRemoteDataSource,weatherLocalDataSource)
                instance=temp
                temp
            }
        }
    }

    override suspend fun getWeather(lat:Double, lon:Double, exclude:String, lang:String, units:String): Flow<WeatherResponse> {
        return weatherRemoteDataSource.getTempOverNetwork(lat,lon,exclude,lang,units)
    }

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): Flow<CurrentWeather> {
        return weatherRemoteDataSource.getCurrentWeatherOverNetwork(lat,lon,lang,units)
    }
}