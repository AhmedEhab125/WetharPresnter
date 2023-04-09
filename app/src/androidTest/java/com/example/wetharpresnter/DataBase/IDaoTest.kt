package com.example.wetharpresnter.DataBase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.wetharpresnter.Models.WeatherData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class IDaoTest{
    @get:Rule
    val instance = InstantTaskExecutorRule()
    private lateinit var database : DataBase.LocationDataBase
    @Before
    fun createDataBase(){
        database= Room.inMemoryDatabaseBuilder(getApplicationContext(), DataBase.LocationDataBase::class.java).build()

    }
    @After
    fun closeDataBase(){
        database.close()
    }
    @Test
    fun getTaskByID_createedTask_getTaskIsTheCreatedTask() = runBlocking{
        // Given
        val weatherData = WeatherData()
        database.locations().insertLocation(weatherData)

        // When
        val loaded = database.locations().getAllLocations()
       var result = loaded.getOrAwaitValue {  }
            assertThat(result , notNullValue())
            assertThat(result.get(0).lat, `is`(weatherData.lat))
            assertThat(result.get(0).timezone, `is`(weatherData.timezone))


        // Then


    }
}