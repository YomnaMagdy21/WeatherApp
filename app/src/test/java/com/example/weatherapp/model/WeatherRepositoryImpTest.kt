package com.example.weatherapp.model

import com.example.weatherapp.database.WeatherLocalDataSource
import com.example.weatherapp.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test


class WeatherRepositoryImpTest{

    lateinit var fakeWeatherRemoteDataSource: WeatherRemoteDataSource
    lateinit var fakeWeatherLocalDataSource: WeatherLocalDataSource
    lateinit var fakeWeatherRepository: WeatherRepository
    val city=City(0,"",null,"",0,0,0,0)



    @Before
    fun setup(){
        fakeWeatherRemoteDataSource=FakeRemoteDataSource()
        fakeWeatherLocalDataSource=FakeWeatherLocalDataSource()
     fakeWeatherRepository= WeatherRepositoryImp(fakeWeatherRemoteDataSource,fakeWeatherLocalDataSource)
    }

    @Test
    fun getWeather_WeatherResponse()= runBlocking{
        //Given
        var lat=0.0
        var lon=0.0
        var unit=""
        var lang=""

        //When
        fakeWeatherRepository.getWeather(0.0,0.0,"","","").collectLatest { data->
            lat=data.lat
            lon=data.lon

            //Then
            assertThat(lat,`is`(data))
            assertThat(lon,`is`(data))
            assertThat(unit,`is`(data))
            assertThat(lang,`is`(data))
        }
    }

    @Test
    fun getHomeWeather_WeatherResponse()= runBlocking{
        //Given
        var lat=0.0
        var lon=0.0
        var unit=""
        var lang=""
        //When
        fakeWeatherRepository.getHomeWeather().collectLatest {data->
            lat=data.lat
            lon=data.lon

            //Then
            assertThat(lat,`is`(data))
            assertThat(lon,`is`(data))
            assertThat(unit,`is`(data))
            assertThat(lang,`is`(data))

        }

    }


    @Test
    fun insertHomeWeather_WeatherResponse_Inserted()= runBlocking{
        //Given
        val homeData=WeatherResponse(0.0,0.0,"",0, listOf(),city)
        fakeWeatherRepository.insertHomeWeather(homeData)

        //When

        fakeWeatherRepository.getHomeWeather().collectLatest {

         //Then
            assertThat(it,not(nullValue()))
        }

    }

}