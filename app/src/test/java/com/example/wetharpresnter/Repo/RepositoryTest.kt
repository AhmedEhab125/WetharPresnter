package com.example.wetharpresnter.Repo

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wetharpresnter.FakeRepo.FakeSource
import com.example.wetharpresnter.MainDispatchersRule
import com.example.wetharpresnter.Models.WeatherData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryTest{
  /*  @Rule
    val rule =InstantTaskExecutorRule()
    @Rule
    val mainDispatchers = MainDispatchersRule()*/


    lateinit var fakeSource: IRepo
    lateinit var app: Application
    @Before
    fun setup(){
        fakeSource=FakeSource()
        app = ApplicationProvider.getApplicationContext()
    }


    @Test
    fun getWetharData_outputWeatherData()= runBlockingTest{
        val expectedResult = WeatherData()
        var temp=WeatherData()
        fakeSource.getWetharData("1.0","10.0").collect{
           temp= it!!
        }
        assertEquals(expectedResult,temp)

    }
    @Test
    fun addLocation_inputWeatherData()= runBlockingTest{
        val data = WeatherData()

        fakeSource.insertFavouriteLocation(app,data)
        fakeSource.getFavouriteLocations(app).collect{
            assertThat(it, CoreMatchers.not(CoreMatchers.nullValue()))
        }

    }
    @Test
    fun delete_inputWeatherData()= runBlockingTest{
        val data = WeatherData()
        fakeSource.insertFavouriteLocation(app,data)
        fakeSource.deleteFavouriteLocation(app,data)
        fakeSource.getFavouriteLocations(app).collect{
            assertThat(it,`is`(emptyList()))
        }

    }



}


