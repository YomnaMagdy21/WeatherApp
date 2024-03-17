package com.example.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite")
data class Favorite (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Long,
    val list: List<WeatherData>,
    val city: City)