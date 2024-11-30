package com.example.weatherapp.alert.view

import com.example.weatherapp.model.WeatherRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AlertBroadcastReceiverEntryPoint {
    fun weatherRepository(): WeatherRepository
}
