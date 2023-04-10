package com.example.wetharpresnter.ViewModel.FavouriteViewModel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.wetharpresnter.Utils.Constants
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Netwoek.ApiState
import com.example.wetharpresnter.Repo.IRepo
import com.example.wetharpresnter.Repo.Repository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouriteViewModel (var context: Context,var iRepo: IRepo) : ViewModel() {
    private var list: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    var accessList: StateFlow<ApiState> = list

    private var favList = MutableLiveData<List<WeatherData>>()
    var accessFavList: LiveData<List<WeatherData>> = favList

    private var updateFlag=true
    var flag = false
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }


    fun getWeatherDataFromApiForFav(
        lat: String,
        lon: String,
        lang: String = "en",
        unit: String = Constants.DEFAULT
    ) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

            Repository.getWetharData(lat, lon, lang, unit).catch { e ->
                list.value = ApiState.Failure(e)
            }.collect { data ->
              //  list.value = ApiState.Success(data)
                if (data != null) {
                    var temp =data
                    temp.timezone=countryName(data.lat, data.lon)
                    iRepo.insertFavouriteLocation(context,temp)
                }
            }

        }
    }



    fun addToFav(lat: String, lon: String, lang: String = "en", unit: String = Constants.DEFAULT) {
        getWeatherDataFromApiForFav(lat, lon, lang, unit)

            viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

                list.collect {result->
                    when(result){
                        is ApiState.Success->{
                            var tempData =   result.date
                            if (tempData != null) {
                                tempData?.timezone = countryName(tempData.lat, tempData.lon)
                                Repository.insertFavouriteLocation(context, tempData)
                                Repository.getFavouriteLocations(context).collect {
                                    favList.postValue(it)
                                }
                            }

                        }
                        else -> {
                            Toast.makeText(context,"error",Toast.LENGTH_LONG).show()
                        }
                    }



            }
        }
    }

    fun getFavLocations() {

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            Log.i("done", "getFavLocations: ")
            iRepo.getFavouriteLocations(context).collect {
                favList.postValue(it)
            }
        }
    }

    fun deleteFromFav(weatherData: WeatherData) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            iRepo.deleteFavouriteLocation(context, weatherData)
        }
    }

    fun updateDatabaseWeatherState() {
        if(updateFlag) {
            updateFlag=false
            Log.i("done", "updateeeeeee: ")
            viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

                var configrations =
                    context.getSharedPreferences("Configuration", Context.MODE_PRIVATE)!!

                iRepo.getFavouriteLocations(context).collect {
                    for (i in 0.. it.size) {
                        var lat = it.get(i).lat
                        var lon =  it.get(i).lon
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
    }
}