package com.example.weatherapp.setting.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor (private val _irepo: WeatherRepository): ViewModel(){

    private val _languageChangeFlow = MutableSharedFlow<String>()
    val languageChangeFlow : SharedFlow<String> = _languageChangeFlow


    fun changeLanguage(language: String) {
        viewModelScope.launch {
            _languageChangeFlow.emit(language)
        }
    }


}