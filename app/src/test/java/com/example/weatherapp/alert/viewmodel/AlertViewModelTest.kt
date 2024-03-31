package com.example.weatherapp.alert.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.MainCoroutineRule
import com.example.weatherapp.getOrAwaitValue
import com.example.weatherapp.home.viewmodel.HomeViewModel
import com.example.weatherapp.model.AlertMessage
import com.example.weatherapp.model.City
import com.example.weatherapp.model.FakeWeatherRepository
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.util.UIState
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class AlertViewModelTest{


    @get:Rule
    val myRule= InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule= MainCoroutineRule()


    lateinit var viewModel: AlertViewModel
    lateinit var viewModel2: HomeViewModel
    lateinit var repo: FakeWeatherRepository
    lateinit var alert1: AlertMessage
    lateinit var alert2: AlertMessage
    lateinit var weatherResponse1: WeatherResponse

    lateinit var city: City
    lateinit var city1: City

    @Before
    fun setUp(){
        repo= FakeWeatherRepository()
        viewModel= AlertViewModel(repo)
        city=City(0,"",null,"",0,0,0,0)

        alert1= AlertMessage(0.0,0.0,"","","","","")
        alert2=AlertMessage(1.0,1.0,"","","","","")
        weatherResponse1= WeatherResponse(0.0,0.0,"",null, listOf(),city)


    }

    @Test
    fun getWeather_AlertMessage()= runBlockingTest{
        viewModel.getWeather(0.0,0.0,"","","")

        //When
        // val value= viewModel.getWeather(0.0,0.0,"","","")
        val value=viewModel.alert.getOrAwaitValue {  }

        //Then
        assertThat(value, `is`(instanceOf(UIState.Success::class.java)))
        val successState = value as UIState.Success<*>
        assertThat(successState.data, `is`(weatherResponse1))


    }
    @Test
    fun getLocalAlertData_AlertMessage(){
        //Given
        viewModel.insertData(alert1)
        viewModel.insertData(alert2)

        //When
        val value=viewModel.alert.getOrAwaitValue {  }

        //Then
        val successState = value as UIState.Success<*>
        assertThat(successState.data, `is`(listOf(alert1,alert2)))

    }

    @Test
    fun insertData_AlertMessage(){
        //Given
        viewModel.insertData(alert1)


        //When
        viewModel.getLocalAlertData()
        val value=viewModel.alert.getOrAwaitValue {  }

        //Then
        val successState = value as UIState.Success<*>
        assertThat(successState.data, `is`(listOf(alert1)))
    }

    @Test
    fun deleteData_AlertMessage(){
        //Given
        viewModel.insertData(alert1)
        viewModel.deleteData(alert1)

        //When
        viewModel.getLocalAlertData()
        val value=viewModel.alert.getOrAwaitValue {  }

        //Then
        val successState = value as UIState.Success<*>
        assertThat(successState.data, `not`(alert1))
    }

}