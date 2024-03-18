package com.example.weatherapp.favorite.view

import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherResponse

interface OnFavoriteClickListener {

    fun onClickToRemove(favorite: Favorite)
    fun goToDetails(favorite: Favorite)
}