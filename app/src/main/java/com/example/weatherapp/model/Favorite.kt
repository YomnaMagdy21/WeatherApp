package com.example.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Favorite")
data class Favorite(
//    @PrimaryKey(autoGenerate = true)
//    var id: Int,
    var lat: Double,
    var lon: Double,
//    var timezone: String,
//    var timezone_offset: Long,
//    var list: List<WeatherData>?,
    @PrimaryKey
    var city: String
): Serializable {

    constructor():this(0.0,0.0,"")
}