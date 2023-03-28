package com.example.wetharpresnter.Repo

import android.content.Context
import com.example.wetharpresnter.DataBase.DataBase
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Netwoek.WeatherService
import kotlinx.coroutines.flow.Flow

class Repository {
    companion object{
        fun getWetharData(lat :String,lon :String ,lang: String="en"): WeatherData? {
            return WeatherService.getWetharData(lat, lon, lang)
        }
        suspend fun getFavouriteLocations(context: Context): Flow<List<WeatherData>> {
           return DataBase.LocationDataBase.getInstance(context).locations().getAllLocations()
        }
        suspend fun insertFavouriteLocation(context: Context, weatherData: WeatherData){
            DataBase.LocationDataBase.getInstance(context).locations().insertLocation(weatherData)
        }
    }
}