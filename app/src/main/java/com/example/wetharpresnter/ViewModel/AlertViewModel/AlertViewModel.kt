package com.example.wetharpresnter.ViewModel.AlertViewModel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.*
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Repo.Repository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertViewModel(var context: Context) : ViewModel()  {
    private var list: MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
    var accessList: LiveData<WeatherData> = list
    private var alertList = MutableLiveData<List<WeatherData>>()

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    fun getWeatherDataFromApi(
        lat: String,
        lon: String,
        lang: String = "en",
        unit: String = Constants.DEFAULT
    ) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

            list.postValue(Repository.getWetharData(lat, lon, lang, unit))

        }
    }
    fun addToalert(lat: String, lon: String, lang: String = "en", unit: String = Constants.DEFAULT) {
        getWeatherDataFromApi(lat, lon, lang, unit)
        list.observe(context as LifecycleOwner) {
            viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                var tempData = it
                tempData.timezone = countryName(it.lat, it.lon)

                Repository.insertFavouriteLocation(context, tempData)
                Repository.getFavouriteLocations(context).collect {
                    alertList.postValue(it)
                }
            }
        }
    }
    private fun countryName(lat: Double, lon: Double): String {
        var address = ""
        var geoCoder: Geocoder = Geocoder(context)
        var addressList = arrayListOf<Address>()
        addressList = geoCoder.getFromLocation(lat, lon, 1) as ArrayList<Address>
        if (addressList.size > 0) {
            address = addressList.get(0).countryName
        }
        return address
    }
}