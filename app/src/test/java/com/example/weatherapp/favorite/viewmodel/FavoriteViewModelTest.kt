package com.example.weatherapp.favorite.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.MainCoroutineRule
import com.example.weatherapp.getOrAwaitValue
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.map.viewmaodel.MapViewModel
import com.example.weatherapp.model.AlertMessage
import com.example.weatherapp.model.City
import com.example.weatherapp.model.FakeWeatherRepository
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.util.UIState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)

class FavoriteViewModelTest{

    @get:Rule
    val myRule= InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule= MainCoroutineRule()


    lateinit var viewModel: FavoriteViewModel
    lateinit var viewModel2: MapViewModel
    lateinit var repo: FakeWeatherRepository
    lateinit var favorite1: Favorite
    lateinit var favorite2: Favorite
    lateinit var weatherResponse1: WeatherResponse
    lateinit var city: City
    lateinit var city1: City
    @Before
    fun setUp(){
        repo=FakeWeatherRepository()
        viewModel= FavoriteViewModel(repo)
        viewModel2=MapViewModel(repo)
        city=City(0,"",null,"",0,0,0,0)
        city1=City(1,"",null,"",1,1,1,1)
        favorite1= Favorite(0.0,0.0,"")
        favorite2= Favorite(1.0,1.0,"2")
        weatherResponse1= WeatherResponse(0.0,0.0,"",null, listOf(),city)

    }



    @Test
    fun getWeather_Favorite()= runBlockingTest{
        viewModel.getWeather(0.0,0.0,"","","")

        //When
        // val value= viewModel.getWeather(0.0,0.0,"","","")
        val value=viewModel.favorites.getOrAwaitValue {  }

        //Then
        assertThat(value, `is`(instanceOf(UIState.Success::class.java)))
        val successState = value as UIState.Success<*>
        assertThat(successState.data, `is`(weatherResponse1))


    }
    @Test
    fun getLocalFavorites_Favorite() = runBlockingTest {
        viewModel2.insertFavorite(favorite1)
        viewModel2.insertFavorite(favorite2)


        // When
        val value=viewModel.favorites.getOrAwaitValue {  }
        val successState = value as UIState.Success<*>

        // Then
        assertThat(successState.data, `is`(listOf(favorite1,favorite2)))

    }


    @Test
    fun deleteFavorite_Favorite(){
        //Given
        viewModel.deleteFavorite(favorite1)


        //When
          viewModel.getLocalFavorites()
        val value=viewModel.favorites.getOrAwaitValue {  }

        //Then
        val successState = value as UIState.Success<*>
        assertThat(successState.data, `not`(favorite1))
    }




    // @Test

}