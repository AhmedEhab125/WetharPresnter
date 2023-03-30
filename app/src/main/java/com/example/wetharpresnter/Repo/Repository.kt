package com.example.wetharpresnter.Repo

import android.content.Context
import androidx.room.Delete
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.DataBase.DataBase
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Netwoek.WeatherService
import kotlinx.coroutines.flow.Flow

class Repository {
    companion object{
        fun getWetharData(lat :String,lon :String ,lang: String="en",unit :String=Constants.DEFAULT): WeatherData? {
            return WeatherService.getWetharData(lat, lon, lang,unit)
        }
        suspend fun getFavouriteLocations(context: Context): Flow<List<WeatherData>> {
           return DataBase.LocationDataBase.getInstance(context).locations().getAllLocations()
        }
        suspend fun insertFavouriteLocation(context: Context, weatherData: WeatherData){
            DataBase.LocationDataBase.getInstance(context).locations().insertLocation(weatherData)
        }
        suspend fun DeleteFavouriteLocation(context: Context, weatherData: WeatherData){
            DataBase.LocationDataBase.getInstance(context).locations().deleteLocation(weatherData)
        }
    }
}