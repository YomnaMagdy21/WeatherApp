//package com.example.weatherapp.favorite.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.weatherapp.model.WeatherRepository
//
//class FavoriteViewModelFactory (private  var _repo: WeatherRepository) :
//    ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
//            FavoriteViewModel(_repo) as T
//        } else {
//            throw IllegalArgumentException("ViewModel class not found")
//
//        }
//    }
//}