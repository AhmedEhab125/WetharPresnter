package com.example.wetharpresnter.FakeRepo

import android.content.Context
import com.example.wetharpresnter.Models.AlertDBModel
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Repo.IRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeRepo :IRepo {
    var favLocation = mutableListOf<WeatherData>()
    var alert = mutableListOf<AlertDBModel>()
    override fun getWetharData(
        lat: String,
        lon: String,
        lang: String,
        unit: String
    ): Flow<WeatherData?> {
        return flowOf(WeatherData())
    }

    override fun getFavouriteLocations(context: Context): Flow<List<WeatherData>> {
        val flowData= flow {
            val storedLocatios=favLocation.toList()
            if(storedLocatios!=null){
                emit(storedLocatios)
            }
        }
        return  flowData
    }

    override suspend fun insertFavouriteLocation(context: Context, weatherData: WeatherData) {
        favLocation.add(weatherData)
    }

    override suspend fun deleteFavouriteLocation(context: Context, weatherData: WeatherData) {
       favLocation.remove(weatherData)
    }

    override suspend fun getAlerts(context: Context): Flow<List<AlertDBModel>> {
      return flowOf(alert)
    }

    override suspend fun insertAlert(context: Context, alertDBModel: AlertDBModel) {
       alert.add(alertDBModel)
    }

    override suspend fun deleteAlert(context: Context, alertDBModel: AlertDBModel) {
       alert.remove(alertDBModel)
    }
}