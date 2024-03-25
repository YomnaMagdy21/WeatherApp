package com.example.weatherapp.home.viewmodel

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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest{

//    @get:Rule
//    val myRule=InstantTaskExecutorRule()
//
//    @get:Rule
//    val mainCoroutineRule= MainCoroutineRule()


    lateinit var viewModel: HomeViewModel
    lateinit var repo:FakeWeatherRepository
    lateinit var weatherResponse1: WeatherResponse
    lateinit var weatherResponse2: WeatherResponse

    lateinit var city: City

    @Before
    fun setUp(){
        repo=FakeWeatherRepository()
        viewModel= HomeViewModel(repo)
        city=City(0,"",null,"",0,0,0,0)
        weatherResponse1= WeatherResponse(0.0,0.0,"",0, listOf(),city)
        weatherResponse2= WeatherResponse(1.0,1.0,"",1, listOf(),city)

    }

   // @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeather_WeatherResponse()=runBlockingTest{
        //Given

        //When
     //  val result= viewModel.getWeather(0.0,0.0,"","","")
        val value=viewModel.weather.getOrAwaitValue {  }
        launch {
            viewModel.weather.collectLatest {
                //Then
                assertThat(value, `is`(not(nullValue())))
            }
        }

    }

    @Test
    fun getLocalData() = runBlockingTest {
        // Given
        viewModel.insertData(weatherResponse1)
        viewModel.insertData(weatherResponse2)

        // When
        val collectedResults = mutableListOf<WeatherResponse>()
        viewModel.weather.collectLatest { result ->
            if (result is UIState.Success<*>) {
                collectedResults.add(result.data as WeatherResponse)
            } else {
                fail("Expected Success, but received $result")
            }
        }

        // Then
        assertThat(collectedResults, `is`(listOf(weatherResponse1, weatherResponse2)))
    }


}