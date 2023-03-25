package com.example.wetharpresnter.ViewModel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wetharpresnter.DataBase.DataBase
import com.example.wetharpresnter.Location.GPSLocation
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private var context: Context) :ViewModel() {
    private var list : MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
    private var favList  = MutableLiveData<List<WeatherData>>()
    var accessList : LiveData<WeatherData> = list
    var accessFavList : LiveData<List<WeatherData>> = favList
    fun getWeatherDataFromApi(lat :String,lon :String) {
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
    fun addToFav(lat :String,lon :String){
        getWeatherDataFromApi(lat,lon)
            accessList.observe(context as LifecycleOwner){
                viewModelScope.launch(Dispatchers.IO){
                    val insertLocation =
                        DataBase.LocationDataBase.getInstance(context).locations().insertLocation(it)

                }
                }
        }
    fun getFavLocations(){
        viewModelScope.launch(Dispatchers.IO) {
            favList.postValue(DataBase.LocationDataBase.getInstance(context).locations().getAllLocations())
        }
    }



}