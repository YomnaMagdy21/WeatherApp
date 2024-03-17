package com.example.weatherapp.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.Alert
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherDAO {

    @Query("SELECT * FROM Home")
    fun getCurrent(): Flow<WeatherResponse>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherResponse: WeatherResponse)
    @Delete
    suspend fun delete(weatherResponse: WeatherResponse): Int

    @Query("SELECT * FROM Favorite")
    fun getAllFavorite(): Flow<List<Favorite>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToFavorite(favorite: Favorite)
    @Delete
    suspend fun deleteFromFavorite(favorite: Favorite): Int

    @Query("SELECT * FROM Alert")
    fun getAllAlerts(): Flow<List<Alert>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToAlert(alert: Alert)
    @Delete
    suspend fun deleteFromAlert(alert: Alert): Int


}