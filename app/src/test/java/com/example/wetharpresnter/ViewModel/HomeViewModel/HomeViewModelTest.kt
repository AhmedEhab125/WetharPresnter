package com.example.wetharpresnter.ViewModel.HomeViewModel

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wetharpresnter.FakeRepo.FakeRepo
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest{
    lateinit var viewModel: HomeViewModel
    lateinit var repo: FakeRepo
    lateinit var firstWeatherData: WeatherData
    lateinit var secondWeatherData: WeatherData
    lateinit var app: Application
    fun fakeData() {
        firstWeatherData = WeatherData()
        secondWeatherData = WeatherData()


    }

    @Before
    fun setup() {
        fakeData()
        app = ApplicationProvider.getApplicationContext()
        repo = FakeRepo()
        viewModel = HomeViewModel(app, repo)

    }
    @Test
    fun getLocation_noInput_allLocations() = runBlockingTest {

        viewModel.getWeatherDataFromApi("11","11")
        var result = viewModel.accessList.getOrAwaitValue { }

        //Then compare the return locations with my test locations

        assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))

    }


}