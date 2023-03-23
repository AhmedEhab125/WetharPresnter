package com.example.wetharpresnter.ViewModel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wetharpresnter.Location.GPSLocation
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private var context: Context) :ViewModel() {
    private var list : MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
    var accessList : LiveData<WeatherData> = list
   private fun getWeatherDataFromApi(lat :String,lon :String) {
        viewModelScope.launch(Dispatchers.IO) {
            list.postValue(Repository.getWetharData(lat, lon))
        }

    }
    fun getLocation(){

        var gpsLocation = GPSLocation(context)
        gpsLocation.getLastLocation()
        gpsLocation.mutable.observe(context as LifecycleOwner ){
            getWeatherDataFromApi(it.second,it.first)
        }
    }


}