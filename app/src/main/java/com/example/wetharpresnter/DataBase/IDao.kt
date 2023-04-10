package com.example.wetharpresnter.DataBase

import androidx.room.*
import com.example.wetharpresnter.Models.AlertDBModel
import com.example.wetharpresnter.Models.WeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface IDao {
    @Query("SELECT * FROM Locations")
     fun getAllLocations(): Flow<List<WeatherData>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(weatherData: WeatherData) :Long
    @Delete
    suspend fun deleteLocation(weatherData: WeatherData)
    @Query("SELECT * FROM Alerts")
    fun getAllAlerts(): Flow<List<AlertDBModel>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alertDBModel: AlertDBModel) :Long
    @Delete
    suspend fun deleteaAlert(alertDBModel: AlertDBModel)
    @Query("DELETE FROM Alerts WHERE ID = :id")
    suspend fun deleteAlertByID(id: Int)


}