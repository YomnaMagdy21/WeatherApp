package com.example.weatherapp.model

data class Hour(var dt:Long,var temp:Double,var feels_like:Double,
var weather:List<Weather>,) {
}