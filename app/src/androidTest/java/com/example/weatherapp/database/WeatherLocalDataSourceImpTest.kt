package com.example.weatherapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.MediumTest
import com.example.weatherapp.getOrAwaitValue
import com.example.weatherapp.model.City
import com.example.weatherapp.model.Favorite
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@MediumTest
class WeatherLocalDataSourceImpTest{

    lateinit var database: WeatherDataBase
    lateinit var localDataSource:WeatherLocalDataSource
    lateinit var city: City
    lateinit var result: Favorite
    lateinit var result2: Favorite

    @get:Rule
    val rule= InstantTaskExecutorRule()

    @Before
    fun setUp(){
        database= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).build()
        localDataSource=WeatherLocalDataSourceImp(ApplicationProvider.getApplicationContext())
        city=City(0,"",null,"",0,0,0,0)
        result= Favorite(0.0,0.0,"1")
        result2= Favorite(1.0,1.0,"2")

    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun getFavData_Favorite()= runBlockingTest{
        //Given
//        val result1 = Favorite(0.0, 0.0, "Location 1")
//        val result2 = Favorite(1.0, 1.0, "Location 2")

        // When
        localDataSource.insertFav(result)
        localDataSource.insertFav(result2)

        // Then
        val storedFavorites = localDataSource.getStoredFavorite().first()

       // assertThat(storedFavorites.size, `is`(2))

        assertThat(storedFavorites, `is`(listOf(result,result2)))

      //  assertThat(storedFavorites[1].lat, `is`(result2.lat))

    }




    @Test
    fun insert_Favorite()= runBlockingTest{
        //Given


        //When
        localDataSource.insertFav(result)

        //Then
        val data=localDataSource.getStoredFavorite().getOrAwaitValue {  }.first()
        assertThat(data,`is`(result))
    }

    @Test
    fun delete_Favorite()= runBlockingTest{
        //Given

        //When
        localDataSource.insertFav(result)
        localDataSource.deleteFav(result)

        //Then
        val deletedData=localDataSource.getStoredFavorite().getOrAwaitValue {  }
        assertThat(deletedData, not(result))

    }



}