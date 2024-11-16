package com.example.weatherapp.di

import com.example.weatherapp.database.WeatherLocalDataSource
import com.example.weatherapp.database.WeatherLocalDataSourceImp
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.model.WeatherRepositoryImp
import com.example.weatherapp.network.WeatherRemoteDataSource
import com.example.weatherapp.network.WeatherRemoteDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SecondModule {

    @Binds
    abstract fun provideRemoteSource(impl: WeatherRemoteDataSourceImp): WeatherRemoteDataSource

    @Binds
    abstract fun provideLocalSource(impl:WeatherLocalDataSourceImp):WeatherLocalDataSource

    @Binds
    abstract fun provideRepository(impl:WeatherRepositoryImp):WeatherRepository


}