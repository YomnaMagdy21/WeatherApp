package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.database.WeatherDAO
import com.example.weatherapp.database.WeatherDataBase
import com.example.weatherapp.network.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class FirstModuleProvider {

    @Provides
    fun provideRetrofit():WeatherService{
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WeatherService::class.java)
    }

    @Provides
    fun providerWeatherDao(@ApplicationContext appContext: Context):WeatherDAO{
        return WeatherDataBase.getInstance(appContext).getWeatherDao()
    }
}