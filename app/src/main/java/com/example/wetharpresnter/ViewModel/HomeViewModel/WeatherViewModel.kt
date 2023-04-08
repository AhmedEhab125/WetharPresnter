package com.example.wetharpresnter.ViewModel.HomeViewModel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.Location.GPSLocation
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Repo.Repository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(var context: Context) : ViewModel() {
    private var list: MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()
    private var favList = MutableLiveData<List<WeatherData>>()
    private var adddTofavList: MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()

    var accessList: LiveData<WeatherData> = list
    var accessFavList: LiveData<List<WeatherData>> = favList

    private var updateFlag=true
    var flag = false
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
   /* fun getWeatherDataFromApiForFav(
        lat: String,
        lon: String,
        lang: String = "en",
        unit: String = Constants.DEFAULT
    ) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

                adddTofavList.postValue(Repository.getWetharData(lat, lon, lang))

        }
    }*/

    fun getLocation(lang: String = "en", unit: String = Constants.DEFAULT) {
        var gpsLocation = GPSLocation(context)
        gpsLocation.getLastLocation()
        gpsLocation.mutable.observe(context as LifecycleOwner) {
            getWeatherDataFromApi(it.second, it.first, lang, unit)

        }
    }

   /* fun addToFav(lat: String, lon: String, lang: String = "en", unit: String = Constants.DEFAULT) {
        getWeatherDataFromApiForFav(lat, lon, lang, unit)
        adddTofavList.observe(context as LifecycleOwner) {
            viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                var tempData = it
                tempData.timezone = countryName(it.lat, it.lon)

                Repository.insertFavouriteLocation(context, tempData)
                Repository.getFavouriteLocations(context).collect {
                    favList.postValue(it)
                }
            }
        }
    }

    fun getFavLocations() {

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            Log.i("done", "getFavLocations: ")
            Repository.getFavouriteLocations(context).collect {
                favList.postValue(it)
            }
        }
    }

    fun deleteFromFav(weatherData: WeatherData) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            Repository.deleteFavouriteLocation(context, weatherData)
        }
    }

    fun updateDatabaseWeatherState() {
        if(updateFlag) {
            updateFlag=false
            Log.i("done", "updateeeeeee: ")
            viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

                var configrations =
                    context.getSharedPreferences("Configuration", Context.MODE_PRIVATE)!!

                Repository.getFavouriteLocations(context).collect {
                    for (data in it) {
                        var lat = data.lat
                        var lon = data.lon
                        configrations.getString(Constants.LANG, "")?.let { it1 ->
                            addToFav(
                                lat.toString(), lon.toString(),
                                it1
                            )
                        }
                    }
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
    }*/

}