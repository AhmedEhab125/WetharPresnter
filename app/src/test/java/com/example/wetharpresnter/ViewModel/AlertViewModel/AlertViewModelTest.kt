package com.example.wetharpresnter.ViewModel.AlertViewModel

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wetharpresnter.FakeRepo.FakeRepo
import com.example.wetharpresnter.Models.AlertDBModel
import com.example.wetharpresnter.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlertViewModelTest {
    /*  @get:Rule
      val rule = InstantTaskExecutorRule()

      @get:Rule
      val mainDispatchersRule = MainDispatchersRule()*/
    // repo -> view model - > creat fake repository
    lateinit var viewModel: AlertViewModel
    lateinit var repo: FakeRepo
    lateinit var firstAlert: AlertDBModel
    lateinit var secondAlert: AlertDBModel
    lateinit var app: Application
    fun fakeData() {
        firstAlert = AlertDBModel()
        secondAlert = AlertDBModel()


    }

    @Before
    fun setup() {
        fakeData()
        app = ApplicationProvider.getApplicationContext()
        repo = FakeRepo()
        viewModel = AlertViewModel(app, repo)

    }

    @Test
    fun getAlert_noInput_allLocations() = runBlockingTest {
        //given
        repo.insertAlert(app, firstAlert)
        repo.insertAlert(app, secondAlert)
        //when
        viewModel.getAlerts()
        var result = viewModel.accessList.getOrAwaitValue { }

        //Then compare the return locations with my test locations

        assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))

    }

    @Test
    fun inserAlert_inputAlert() {
        viewModel.addToAlert("23", "23", 12, 13L, "asd", 12L, "asd", "ads")
        viewModel.addToAlert("23", "23", 12, 13L, "asd", 12L, "asd", "ads")

        viewModel.getAlerts()
        var result = viewModel.accessList.getOrAwaitValue { }
        assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
    }


}