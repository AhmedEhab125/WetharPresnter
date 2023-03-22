package com.example.wetharpresnter.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Netwoek.Repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel() :ViewModel() {
    private var list : MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
    var accessList : LiveData<WeatherData> = list
    fun getWeatherDataFromApi(lat :String,lon :String) {
        viewModelScope.launch(Dispatchers.IO) {
            list.postValue(Repository.getWetharData(lat, lon))
        }

    }
}