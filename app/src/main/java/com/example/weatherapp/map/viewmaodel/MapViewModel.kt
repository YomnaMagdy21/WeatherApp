package com.example.weatherapp.map.viewmaodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor (private val _irepo:WeatherRepository): ViewModel(){

    private val _favs: MutableStateFlow<List<Favorite>> = MutableStateFlow(emptyList())
    val favs: StateFlow<List<Favorite>> = _favs


    fun insertFavorite(favorite: Favorite){
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.insertFavoriteWeather(favorite)


        }
    }



}