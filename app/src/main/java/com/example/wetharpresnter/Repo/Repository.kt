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
    companion object : IRepo {
        override fun getWetharData(lat :String, lon :String, lang: String, unit :String): Flow<WeatherData?> {
            return  flowOf(WeatherService.getWetharData(lat, lon, lang,unit))
        }
         override fun getFavouriteLocations(context: Context): Flow<List<WeatherData>> {
           return DataBase.LocationDataBase.getInstance(context).locations().getAllLocations()
        }
        override suspend fun insertFavouriteLocation(context: Context, weatherData: WeatherData){
            DataBase.LocationDataBase.getInstance(context).locations().insertLocation(weatherData)
        }
        override suspend fun deleteFavouriteLocation(context: Context, weatherData: WeatherData){
            DataBase.LocationDataBase.getInstance(context).locations().deleteLocation(weatherData)
        }
        override suspend fun  getAlerts(context: Context): Flow<List<AlertDBModel>>{
            return DataBase.LocationDataBase.getInstance(context).locations().getAllAlerts()
        }
        override suspend fun insertAlert(context: Context, alertDBModel: AlertDBModel){
            DataBase.LocationDataBase.getInstance(context).locations().insertAlert(alertDBModel)
        }
        override suspend fun deleteAlert(context: Context, alertDBModel: AlertDBModel){
            DataBase.LocationDataBase.getInstance(context).locations().deleteaAlert(alertDBModel)
        }

        override suspend fun deleteAlertById(context: Context, id: Int) {
            DataBase.LocationDataBase.getInstance(context).locations().deleteAlertByID(id)
        }
    }
}