package com.example.wetharpresnter.Repo

import android.content.Context
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.Models.AlertDBModel
import com.example.wetharpresnter.Models.WeatherData
import kotlinx.coroutines.flow.Flow

interface IRepo {
    fun getWetharData(
        lat: String,
        lon: String,
        lang: String = "en",
        unit: String = Constants.DEFAULT
    ): Flow<WeatherData?>

    fun getFavouriteLocations(context: Context): Flow<List<WeatherData>>

    suspend fun insertFavouriteLocation(context: Context, weatherData: WeatherData)
    suspend fun deleteFavouriteLocation(context: Context, weatherData: WeatherData)
    suspend fun getAlerts(context: Context): Flow<List<AlertDBModel>>
    suspend fun insertAlert(context: Context, alertDBModel: AlertDBModel)
    suspend fun deleteAlert(context: Context, alertDBModel: AlertDBModel)
    suspend fun deleteAlertById(context: Context, id:Int)
}