package com.example.weatherapp.model

data class Current(var dt:Long,var sunrise:Long,var sunset:Long,var temp:Double,var feels_like:Double,
    var pressure:Int,var humidity:Int,var dew_point:Double,var uvi:Double,var clouds:Int,var visibility:Int,
    var wind_speed:Double,var wind_deg:Int,var wind_gust:Double,var weather:List<Weather>) {
}