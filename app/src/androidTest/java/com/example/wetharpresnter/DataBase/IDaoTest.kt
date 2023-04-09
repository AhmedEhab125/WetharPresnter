package com.example.wetharpresnter.DataBase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.wetharpresnter.Models.AlertDBModel
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
    fun insertLocations_createedLocation_getLocation() = runBlocking{
        // Given
        val weatherData = WeatherData()
        database.locations().insertLocation(weatherData)

        // When
        val loaded = database.locations().getAllLocations()
       var result = loaded.getOrAwaitValue {  }
        // Then
            assertThat(result , notNullValue())
            assertThat(result.get(0).lat, `is`(weatherData.lat))
            assertThat(result.get(0).timezone, `is`(weatherData.timezone))
    }
    @Test
    fun deleteLocations_createedLocation_getLocation() = runBlocking{
        // Given
        val weatherData = WeatherData()
        database.locations().insertLocation(weatherData)
        database.locations().deleteLocation(weatherData)

        // When
        val loaded = database.locations().getAllLocations()
        var result = loaded.getOrAwaitValue {  }
        // Then
        assertThat(result,`is`(emptyList()))

    }
    @Test
    fun insertAlert_createedAlert_getAlert() = runBlocking{
        // Given
        val alert = AlertDBModel()
        database.locations().insertAlert(alert)


        // When
        val loaded = database.locations().getAllAlerts()
        var result = loaded.getOrAwaitValue {  }
        // Then
        assertThat(result , notNullValue())
        assertThat(result.get(0).lat, `is`(alert.lat))
        assertThat(result.get(0).ID, `is`(alert.ID))
    }
    @Test
    fun deleteAlert_createedAlert_getAlerts() = runBlocking{
        // Given
        val alert = AlertDBModel()
        database.locations().insertAlert(alert)
        database.locations().deleteaAlert(alert)

        // When
        val loaded = database.locations().getAllAlerts()
        var result = loaded.getOrAwaitValue {  }
        // Then
        assertThat(result,`is`(emptyList()))

    }


}