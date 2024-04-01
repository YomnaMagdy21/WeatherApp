package com.example.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Favorite")
data class Favorite(
    var lat: Double,
    var lon: Double,

    @PrimaryKey
    var city: String
): Serializable {

    constructor():this(0.0,0.0,"")
}