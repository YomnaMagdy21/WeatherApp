package com.example.weatherapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapp.getOrAwaitValue
import com.example.weatherapp.model.City
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class WeatherDAOTest {

    lateinit var database: WeatherDataBase
    lateinit var dao: WeatherDAO
    lateinit var city: City
    lateinit var result:WeatherResponse
    lateinit var result2:WeatherResponse

    @get:Rule
    val rule= InstantTaskExecutorRule()

    @Before
    fun setUp(){
        database= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).build()
        dao=database.getWeatherDao()
        city=City(0,"",null,"",0,0,0,0)
         result= WeatherResponse(0.0,0.0,"",0, listOf(),city)
        result2= WeatherResponse(0.0,0.0,"",0, listOf(),city)

    }


    @After
    fun tearDown(){
        database.close()
    }


    @Test
    fun getHomeData_WeatherResponse()= runBlockingTest{
        //Given
        //  val result= WeatherResponse(0.0,0.0,"",0, listOf(),city)

        //When
        database.getWeatherDao().insert(result)
        database.getWeatherDao().insert(result2)

        //Then
        val data=database.getWeatherDao().getCurrent().getOrAwaitValue {  }
        assertThat(data,`is`(result))
        assertThat(data,`is`(result2))
    }
    @Test
    fun insert_WeatherResponse()= runBlockingTest{
        //Given
     //  val result= WeatherResponse(0.0,0.0,"",0, listOf(),city)

        //When
        database.getWeatherDao().insert(result)

        //Then
        val data=database.getWeatherDao().getCurrent().getOrAwaitValue {  }
        assertThat(data,`is`(result))
    }

    @Test
    fun delete_WeatherResponse()= runBlockingTest{
        //Given

        //When
        database.getWeatherDao().delete()

        //Then
        val deletedData=database.getWeatherDao().getCurrent().getOrAwaitValue {  }
        assertThat(deletedData,`is`(nullValue()))

    }


}