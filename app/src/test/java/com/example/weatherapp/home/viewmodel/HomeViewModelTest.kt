package com.example.weatherapp.home.viewmodel

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.MainCoroutineRule
import com.example.weatherapp.getOrAwaitValue
import com.example.weatherapp.model.City

import com.example.weatherapp.model.FakeWeatherRepository
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.util.UIState
import junit.framework.TestCase.fail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeViewModelTest{

    @get:Rule
    val myRule=InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule= MainCoroutineRule()


    lateinit var viewModel: HomeViewModel
    lateinit var repo:FakeWeatherRepository
    lateinit var weatherResponse1: WeatherResponse
    lateinit var weatherResponse2: WeatherResponse

    lateinit var city: City
    lateinit var city1: City

    @Before
    fun setUp(){
        repo=FakeWeatherRepository()
        viewModel= HomeViewModel(repo)
        city=City(0,"",null,"",0,0,0,0)
        city1=City(1,"",null,"",1,1,1,1)
        weatherResponse1= WeatherResponse(0.0,0.0,"",null, listOf(),city)
        weatherResponse2= WeatherResponse(1.0,1.0,"",1, listOf(),city)

    }

   @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeather_WeatherResponse()=runBlockingTest{
        //Given
          viewModel.getWeather(0.0,0.0,"","","")

        //When
      // val value= viewModel.getWeather(0.0,0.0,"","","")
        val value=viewModel.weather.getOrAwaitValue {  }

       //Then
       assertThat(value, `is`(instanceOf(UIState.Success::class.java)))
       val successState = value as UIState.Success<*>
       assertThat(successState.data, `is`(weatherResponse1))



    }

    @Test
    fun getLocalData_WeatherResponse() = runBlockingTest {
        // Given
        viewModel.insertData(weatherResponse1)


        // When
        val value=viewModel.weather.getOrAwaitValue {  }
        val successState = value as UIState.Success<*>

        // Then
        assertThat(successState.data, `is`(weatherResponse1))
    }

    @Test
    fun deleteData() = runBlockingTest {
        // Given

        viewModel.insertData(weatherResponse2)
        viewModel.deleteData()

        // When

        val value = viewModel.weather.getOrAwaitValue {  }


        val successState = value as UIState.Success<*>

        // Then
        assertThat(successState, `is`(nullValue()))
//
    }






}