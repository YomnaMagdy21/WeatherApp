package com.example.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//data class WeatherResponse(var lat:Double,var lon:Double,var timezone:String,var timezone_offset:Long,
//    var current: Current,var  hourly:List<Hour>,var daily:List<Day>,var alerts:List<Alert>) {
//}
@Entity(tableName = "Home")
data class WeatherResponse(
//    @PrimaryKey(autoGenerate = true)
//    var id: Int,
    var lat: Double,
    var lon: Double,
    var timezone: String?,
    var timezone_offset: Long?,
    var list: List<WeatherData>,
    @PrimaryKey
    var city: City
){
 constructor():this(0.0,0.0,"",null, listOf(),City(0,"",null,"",0,0,0,0))
}



data class WeatherData(
    var dt: Long,
    var main: Main,
    var weather: List<Weather>,
    var clouds: Clouds,
    var wind: Wind,
    var visibility: Int,
    var pop: Double,
    var rain: Rain?,
    var sys: Sys,
    var dt_txt: String
)

data class Main(
    var temp: Double,
    var feels_like: Double,
    var temp_min: Double,
    var temp_max: Double,
    var pressure: Int,
    var sea_level: Int,
    var grnd_level: Int,
    var humidity: Int,
    var temp_kf: Double
)

data class Weather(
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
)

data class Clouds(
    var all: Int
)

data class Wind(
    var speed: Double,
    var deg: Int,
    var gust: Double
)

data class Rain(
    var `3h`: Double
)

data class Sys(
    var pod: String
)

data class City(
    var id: Long,
    var name: String,
    var coord: Coord?,
    var country: String,
    var population: Int,
    var timezone: Int,
    var sunrise: Long,
    var sunset: Long
)

data class Coord(
    var lat: Double,
    var lon: Double
)
