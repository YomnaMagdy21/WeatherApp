package com.example.weatherapp.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.AlertMessage
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.util.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel(private val _irepo: WeatherRepository): ViewModel()  {
    private var _alert: MutableStateFlow<UIState> = MutableStateFlow(UIState.Loading)
    val alert: MutableStateFlow<UIState> = _alert


    init {
        getLocalAlertData()
    }

    fun deleteData(alertMessage: AlertMessage){
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.deleteAlertWeather(alertMessage)
            getLocalAlertData()

        }
    }

    fun insertData(alertMessage: AlertMessage){
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.insertAlertWeather(alertMessage)


        }
    }

    fun getLocalAlertData()= viewModelScope.launch{
        _irepo.getAlertWeather()
            .catch { e->
                _alert.value= UIState.Failure(e)
            }
            .collect{
                _alert.value=UIState.Success(it)
            }            }

    fun getWeather(lat: Double, lon:Double, exclude:String, units:String, lang:String){
        viewModelScope.launch(Dispatchers.IO){
            //  _weather.postValue(_irepo.getWeather(lat,lon,exclude,units,lang))
            _irepo.getWeather(lat,lon,exclude,units,lang)
                .catch {
                        e->
                    _alert.value= UIState.Failure(e)
                }
                .collect{
                    _alert.value= UIState.Success(it)
                }
        }
    }

}