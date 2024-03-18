package com.example.weatherapp.setting.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.WeatherRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SettingViewModel  (private val _irepo: WeatherRepository): ViewModel(){

    private val _languageChangeFlow = MutableSharedFlow<String>()
    val languageChangeFlow = _languageChangeFlow.asSharedFlow()

    fun changeLanguage(language: String) {
        _languageChangeFlow.tryEmit(language)
    }
}