package com.example.weatherapp.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.Hour
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.util.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val _irepo:WeatherRepository): ViewModel() {
    private var _weather: MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)
    val weather: MutableStateFlow<UIState> = _weather

    init {
        getLocalData()
      //  getWeather(37.4220936,-122.083922,"","","")
      //  geCurrentWeather(37.4220936,-122.083922,"","")
    }
//

    fun deleteData(){
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.deleteHomeWeather()
            getLocalData()

        }
    }

    fun insertData(weatherResponse: WeatherResponse){
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.insertHomeWeather(weatherResponse)


        }
    }

     fun getLocalData()= viewModelScope.launch{
        _irepo.getHomeWeather()
            .catch { e->
                _weather.value= UIState.Failure(e)
            }
            .collect{
                _weather.value=UIState.Success(it)
            }            }


    fun getWeather(lat:Double, lon:Double, exclude:String,  units:String,lang:String){
        viewModelScope.launch(Dispatchers.IO){
          //  _weather.postValue(_irepo.getWeather(lat,lon,exclude,units,lang))
            _irepo.getWeather(lat,lon,exclude,units,lang)
                .catch {
                        e->
                    _weather.value= UIState.Failure(e)
                }
                .collect{
                    _weather.value= UIState.Success(it)
                }
        }
    }

    private fun geCurrentWeather(lat:Double, lon:Double, lang:String, units:String){
        viewModelScope.launch(Dispatchers.IO){
            //  _weather.postValue(_irepo.getWeather(lat,lon,exclude,lang,units))
            _irepo.getCurrentWeather(lat,lon,lang,units)
                .catch {
                        e->
                    _weather.value= UIState.Failure(e)
                }
                .collect{
                    _weather.value= UIState.Success(it)
                }
        }
    }
}