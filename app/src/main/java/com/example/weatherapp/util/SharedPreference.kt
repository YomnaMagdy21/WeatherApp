package com.example.weatherapp.util

import android.content.Context

object SharedPreference {

    private const val PREF_NAME = "MyAppPrefs"
    private const val KEY_LANGUAGE = "language"
    private const val KEY_LOCATION = "location"
    private const val KEY_UNITS = "units"
    private const val KEY_WIND_SPEED = "wind_speed"

    fun saveUnit(context: Context, unite: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_UNITS, unite).apply()
    }

    fun getUnit(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_UNITS, "") ?: ""
    }

    fun saveWindUnit(context: Context, unite: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_WIND_SPEED, unite).apply()
    }

    fun getWindUnit(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_WIND_SPEED, "") ?: ""
    }
}