package com.example.weatherapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.Alert
import com.example.weatherapp.model.AlertData
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
@Dao
interface WeatherDAO {

    @Query("SELECT * FROM Home")
    fun getCurrent(): Flow<WeatherResponse>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weatherResponse: WeatherResponse)
    @Query("DELETE FROM Home")
    suspend fun delete(): Int

    @Query("SELECT * FROM Favorite")
    fun getAllFavorite(): Flow<List<Favorite>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToFavorite(favorite: Favorite)
    @Delete
    suspend fun deleteFromFavorite(favorite: Favorite): Int

    @Query("SELECT * FROM Alert")
    fun getAllAlerts(): Flow<List<AlertData>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToAlert(alert: AlertData)
    @Delete
    suspend fun deleteFromAlert(alert: AlertData): Int


}