package com.example.weatherapp.database

import androidx.room.TypeConverter
import com.example.weatherapp.model.City
import com.example.weatherapp.model.Clouds
import com.example.weatherapp.model.Main
import com.example.weatherapp.model.Message
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.WeatherData
import com.example.weatherapp.model.Wind
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherDataConverter {


    private val gson = Gson()

    @TypeConverter
    fun fromWeatherDataList(weatherDataList: List<WeatherData>?): String? {
        return gson.toJson(weatherDataList)
    }

    @TypeConverter
    fun toWeatherList(jsonString: String?): List<WeatherData>? {
         val type = object : TypeToken<List<WeatherData>>() {}.type
        return gson.fromJson<List<WeatherData>>(jsonString, type)
    }

    @TypeConverter
    fun fromCity(data: City?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toCity(jsonString: String?): City? {
        return gson.fromJson(jsonString,City::class.java)
    }

    @TypeConverter
    fun fromMessage(data: Message?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toMessage(jsonString: String?): Message? {
        return gson.fromJson(jsonString,Message::class.java)
    }



//    @TypeConverter
//    fun fromMain(data: Main?): String? {
//        return gson.toJson(data)
//    }
//
//    @TypeConverter
//    fun toMain(jsonString: String?): Main? {
//        return gson.fromJson(jsonString,Main::class.java)
//    }
//
//    @TypeConverter
//    fun fromWeatherList(weatherDataList: List<WeatherData>?): String? {
//        return gson.toJson(weatherDataList)
//    }
//
//    @TypeConverter
//    fun toListOfWeather(jsonString: String?): List<Weather>? {
//        val type = object : TypeToken<List<Weather>>() {}.type
//        return gson.fromJson<List<Weather>>(jsonString, type)
//    }
//
//    @TypeConverter
//    fun fromClouds(data: Clouds?): String? {
//        return gson.toJson(data)
//    }
//
//    @TypeConverter
//    fun toClouds(jsonString: String?): Clouds? {
//        return gson.fromJson(jsonString,Clouds::class.java)
//    }
//
//    @TypeConverter
//    fun fromWind(data: Wind?): String? {
//        return gson.toJson(data)
//    }
//
//    @TypeConverter
//    fun toCWind(jsonString: String?): Wind? {
//        return gson.fromJson(jsonString,Wind::class.java)
  //  }
}