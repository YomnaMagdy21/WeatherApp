package com.example.weatherapp.setting.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.WeatherRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingViewModel  (private val _irepo: WeatherRepository): ViewModel(){

    private val _languageChangeFlow = MutableSharedFlow<String>()
    val languageChangeFlow :MutableSharedFlow<String> = _languageChangeFlow


    fun changeLanguage(language: String) {
        viewModelScope.launch {
            _languageChangeFlow.emit(language)
        }
    }


}