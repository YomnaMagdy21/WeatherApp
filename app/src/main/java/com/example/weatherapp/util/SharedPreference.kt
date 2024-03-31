package com.example.weatherapp.util

import android.content.Context

object SharedPreference {

    private const val PREF_NAME = "MyAppPrefs"
    private const val KEY_LANGUAGE = "language"
    private const val KEY_LOCATION = "location"
    private const val KEY_UNITS = "units"
    private const val KEY_WIND_SPEED = "wind_speed"
    private const val KEY_LAT = "lat"
    private const val KEY_LON = "lon"
    private const val KEY_INITIAL = "KEY_INITIAL"


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

    fun saveLocation(context: Context, unite: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_WIND_SPEED, unite).apply()
    }

    fun getLocation(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_WIND_SPEED, "") ?: ""
    }
    fun saveLat(context: Context, unite: Double) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LAT, unite.toString()).apply()
    }

    fun getLat(context: Context): Double {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LAT, "")?.toDoubleOrNull() ?: 0.0
    }
    fun saveLon(context: Context, unite: Double) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LON, unite.toString()).apply()
    }

    fun getLon(context: Context): Double {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LON, "")?.toDoubleOrNull() ?: 0.0
    }
    fun saveLanguage(context: Context, unite: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, unite).apply()
    }

    fun getLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, "") ?: ""
    }

    fun saveInitialTime(context: Context, unite: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_INITIAL, unite).apply()
    }

    fun getInitialTime(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_INITIAL, "") ?: "first"
    }



}