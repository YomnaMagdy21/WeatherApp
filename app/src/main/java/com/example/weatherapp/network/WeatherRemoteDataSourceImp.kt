package com.example.weatherapp.network

import android.util.Log
import com.example.weatherapp.model.WeatherResponse

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

    override suspend fun getTempOverNetwork(
        lat: Double,
        lon: Double,
        exclude: String,
        units:String,
        lang:String
    ): WeatherResponse {
        return weatherService.getWeather(lat,lon,exclude,units,lang)
    }
}