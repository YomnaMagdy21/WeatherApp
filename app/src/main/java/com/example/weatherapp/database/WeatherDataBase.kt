package com.example.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.model.Alert
import com.example.weatherapp.model.AlertData
import com.example.weatherapp.model.AlertMessage
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherResponse


@Database(entities =[WeatherResponse::class,Favorite::class, AlertMessage::class], version = 6 )
@TypeConverters(WeatherDataConverter::class)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDAO
    companion object{
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getInstance (ctx: Context): WeatherDataBase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, WeatherDataBase::class.java, "weather_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance }
        }
    }
}