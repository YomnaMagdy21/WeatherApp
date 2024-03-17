package com.example.weatherapp.util

sealed class UIState {

    data class Success<T>(val data: T) : UIState()
    data class Failure(val msg: Throwable) : UIState()
    object Loading : UIState()
}