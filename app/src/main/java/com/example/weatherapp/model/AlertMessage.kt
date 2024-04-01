package com.example.weatherapp.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.model.Weather

@Entity(tableName = "Alert")
data class AlertMessage (
    @PrimaryKey(autoGenerate = true)
var id: Int,
    var lat:Double,var lon:Double,var city:String,var fromDate:String, var fromTime:String, var toDate:String, var toTime:String){


    constructor(lat:Double ,lon:Double,city:String, fromDate:String,  fromTime:String,  toDate:String,  toTime:String):this(0,lat,lon,city,fromDate,fromTime,toDate,toTime)
}