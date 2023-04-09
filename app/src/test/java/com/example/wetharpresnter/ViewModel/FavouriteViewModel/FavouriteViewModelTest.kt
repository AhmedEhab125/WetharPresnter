package com.example.wetharpresnter.ViewModel.FavouriteViewModel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Delete
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wetharpresnter.FakeRepo.FakeRepo
import com.example.wetharpresnter.MainDispatchersRule
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FavouriteViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()
    // repo -> view model - > creat fake repository
    lateinit var viewModel :FavouriteViewModel
    lateinit var repo : FakeRepo
    lateinit var firstWeatherData: WeatherData
    lateinit var secondWeatherData: WeatherData
    lateinit var app: Application
    fun fakeData(){
        firstWeatherData= WeatherData(100.0,200.0)
        secondWeatherData= WeatherData(300.0,200.0)


    }
    @Before
    fun setup(){
        fakeData()
        app = ApplicationProvider.getApplicationContext()
        repo  = FakeRepo()
        viewModel= FavouriteViewModel(app,repo)

    }
    @Test
    fun getFavouriteLocation_noInput_allLocations()= runBlockingTest{
        //given
        repo.insertFavouriteLocation(app,firstWeatherData)
        repo.insertFavouriteLocation(app,secondWeatherData)
        //when
        viewModel.getFavLocations()
        var result=viewModel.accessFavList.getOrAwaitValue {  }

        //Then compare the return locations with my test locations


        assertThat(result,not(nullValue()))

    }
    @Test
    fun deleteFavouriteLocation_noInput_allLocations()= runBlockingTest{
        repo.insertFavouriteLocation(app,firstWeatherData)
        repo.insertFavouriteLocation(app,secondWeatherData)
        viewModel.deleteFromFav(firstWeatherData)
        viewModel.getFavLocations()
        var result=viewModel.accessFavList.getOrAwaitValue {  }

        //Then compare the return locations with my test locations


        assertThat(result,`is`(listOf(secondWeatherData)))

    }
    @Test
    fun deleteFavouriteLocation_noInput_noLocation()= runBlockingTest{
        viewModel.addToFav("100","100","en")
        viewModel.addToFav("100","100","en")
        viewModel.deleteFromFav(firstWeatherData)
        viewModel.deleteFromFav(secondWeatherData)
        viewModel.getFavLocations()
        var result=viewModel.accessFavList.getOrAwaitValue {  }

        //Then compare the return locations with my test locations


        assertThat(result,`is`(emptyList()))

    }
    @Test
    fun insertFavouriteLocation_noInput_noLocation()= runBlockingTest{
        viewModel.addToFav("100","100","en")
        viewModel.getFavLocations()
        var result=viewModel.accessFavList.getOrAwaitValue {  }

        //Then compare the return locations with my test locations


        assertThat(result,not(nullValue()))

    }

}