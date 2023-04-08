package com.example.wetharpresnter.Repo

import android.content.Context
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.DataBase.DataBase
import com.example.wetharpresnter.Models.AlertDBModel
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Netwoek.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class Repository {
    companion object{
        fun getWetharData(lat :String,lon :String ,lang: String="en",unit :String=Constants.DEFAULT): Flow<WeatherData?> {
            return  flowOf(WeatherService.getWetharData(lat, lon, lang,unit))
        }
         fun getFavouriteLocations(context: Context): Flow<List<WeatherData>> {
           return DataBase.LocationDataBase.getInstance(context).locations().getAllLocations()
        }
        suspend fun insertFavouriteLocation(context: Context, weatherData: WeatherData){
            DataBase.LocationDataBase.getInstance(context).locations().insertLocation(weatherData)
        }
        suspend fun deleteFavouriteLocation(context: Context, weatherData: WeatherData){
            DataBase.LocationDataBase.getInstance(context).locations().deleteLocation(weatherData)
        }
        suspend fun  getAlerts(context: Context): Flow<List<AlertDBModel>>{
            return DataBase.LocationDataBase.getInstance(context).locations().getAllAlerts()
        }
        suspend fun insertAlert(context: Context, alertDBModel: AlertDBModel){
            DataBase.LocationDataBase.getInstance(context).locations().insertAlert(alertDBModel)
        }
        suspend fun deleteAlert(context: Context, alertDBModel: AlertDBModel){
            DataBase.LocationDataBase.getInstance(context).locations().deleteaAlert(alertDBModel)
        }
    }
}