package com.example.weatherapp.network

import android.util.Log
import com.example.weatherapp.CurrentWeather
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSourceImp :WeatherRemoteDataSource{

    val weatherService:WeatherService by lazy {
        RetrofitHelper.retrofitInstance.create(WeatherService::class.java)
    }



    companion object{
        private var instance:WeatherRemoteDataSourceImp?=null
        fun getInstance():WeatherRemoteDataSourceImp{
            return instance?: synchronized(this){
                val temp=WeatherRemoteDataSourceImp()
                instance=temp
                temp
            }
        }
    }

    override  fun getTempOverNetwork(
        lat: Double,
        lon: Double,
        exclude: String,
        units:String,
        lang:String
    ): Flow<WeatherResponse> {
        return flow {
            emit(weatherService.getWeather(lat,lon,exclude,units,lang))
        }
    }

    override suspend fun getCurrentWeatherOverNetwork(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): Flow<CurrentWeather> {
        return flow {
            emit(weatherService.getCurrentWeather(lat,lon,units,lang))
        }
    }




}