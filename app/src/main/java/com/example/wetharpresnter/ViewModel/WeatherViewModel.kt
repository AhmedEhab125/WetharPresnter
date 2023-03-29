package com.example.wetharpresnter.ViewModel

import android.content.Context
import android.util.Log
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

class WeatherViewModel(private var context: Context) : ViewModel() {
    private var list: MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
    private var favList = MutableLiveData<List<WeatherData>>()
    private var adddTofavList: MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
    var accessAddTofavList: LiveData<WeatherData> = adddTofavList

    var accessList: LiveData<WeatherData> = list
    var accessFavList: LiveData<List<WeatherData>> = favList

    var flag =false
    fun getWeatherDataFromApi(lat: String, lon: String,lang :String="en") {
        viewModelScope.launch(Dispatchers.IO) {
            if(flag==true){
                adddTofavList.postValue(Repository.getWetharData(lat, lon,lang))
            }
            list.postValue(Repository.getWetharData(lat, lon,lang))
        }
    }

    fun getLocation(lang: String = "en") {
        var gpsLocation = GPSLocation(context)
        gpsLocation.getLastLocation()
        gpsLocation.mutable.observe(context as LifecycleOwner) {
            getWeatherDataFromApi(it.second, it.first,lang)

        }
    }

    fun addToFav(lat: String, lon: String) {
        flag=true
        getWeatherDataFromApi(lat, lon)
        adddTofavList.observe(context as LifecycleOwner) {
            viewModelScope.launch(Dispatchers.IO) {
                Repository.insertFavouriteLocation(context,it)
                Repository.getFavouriteLocations(context).collect{
                    favList.postValue(it)
                    flag=false
                }
            }
        }
    }

    fun getFavLocations() {

        viewModelScope.launch(Dispatchers.IO) {
            Log.i("done", "getFavLocations: ")
            Repository.getFavouriteLocations(context).collect{
                favList.postValue(it)
            }
        }
    }
    fun deleteFromFav(weatherData: WeatherData){
        viewModelScope.launch(Dispatchers.IO) {
            Repository.DeleteFavouriteLocation(context,weatherData)
        }
    }


}