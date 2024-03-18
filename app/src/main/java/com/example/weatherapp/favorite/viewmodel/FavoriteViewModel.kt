package com.example.weatherapp.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.util.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel (private val _irepo: WeatherRepository): ViewModel(){

    private var _favorite: MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)
    val favorites: MutableStateFlow<UIState> = _favorite

    init {

        getLocalFavorites()
    }
    fun deleteFavorite(favorite: Favorite){
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.deleteFavoriteWeather(favorite)
            getLocalFavorites()

        }
    }
   private fun getLocalFavorites()= viewModelScope.launch{
        _irepo.getFavoriteWeather()
            .catch { e->
                _favorite.value= UIState.Failure(e)
            }
            .collect{
                _favorite.value=UIState.Success(it)
            }            }

     fun getWeather(lat:Double, lon:Double, exclude:String,units :String, lang:String){
        viewModelScope.launch(Dispatchers.IO){
            //  _weather.postValue(_irepo.getWeather(lat,lon,exclude,lang,units))
            _irepo.getWeather(lat,lon,exclude,units,lang)
                .catch {
                        e->
                    _favorite.value= UIState.Failure(e)
                }
                .collect{
                    _favorite.value= UIState.Success(it)
                }
        }
    }
}
