package com.example.wetharpresnter.FakeRepo

import android.content.Context
import com.example.wetharpresnter.Models.AlertDBModel
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Repo.IRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSource :IRepo
{
    var mydata= mutableListOf<WeatherData>()
    override fun getWetharData(
        lat: String,
        lon: String,
        lang: String,
        unit: String
    ): Flow<WeatherData?> {
     return flowOf(WeatherData())
    }

    override fun getFavouriteLocations(context: Context): Flow<List<WeatherData>> {
        return flowOf(mydata)
    }

    override suspend fun insertFavouriteLocation(context: Context, weatherData: WeatherData) {
       mydata.add(weatherData)
    }

    override suspend fun deleteFavouriteLocation(context: Context, weatherData: WeatherData) {
        mydata.remove(weatherData)
    }

    override suspend fun getAlerts(context: Context): Flow<List<AlertDBModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(context: Context, alertDBModel: AlertDBModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(context: Context, alertDBModel: AlertDBModel) {
        TODO("Not yet implemented")
    }
}