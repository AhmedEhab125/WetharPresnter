package com.example.wetharpresnter.DataBase

import androidx.room.*
import com.example.wetharpresnter.Models.WeatherData

@Dao
interface IDao {
    @Query("SELECT * FROM Locations")
    suspend fun getAllLocations(): List<WeatherData>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(weatherData: WeatherData) :Long
    @Delete
    suspend fun deleteLocation(weatherData: WeatherData)
}