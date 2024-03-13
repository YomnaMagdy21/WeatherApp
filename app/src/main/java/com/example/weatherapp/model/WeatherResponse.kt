package com.example.weatherapp.model

data class WeatherResponse(var lat:Double,var lon:Double,var timezone:String,var timezone_offset:Long,
    var current: Current,var  hourly:List<Hour>,var daily:List<Day>,var alerts:List<Alert>) {
}