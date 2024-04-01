package com.example.weatherapp.map.viewmaodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.util.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MapViewModel  (private val _irepo:WeatherRepository): ViewModel(){

    private val _favs: MutableStateFlow<List<Favorite>> = MutableStateFlow(emptyList())
    val favs: MutableStateFlow<List<Favorite>> = _favs


    fun insertFavorite(favorite: Favorite){
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.insertFavoriteWeather(favorite)


        }
    }



}