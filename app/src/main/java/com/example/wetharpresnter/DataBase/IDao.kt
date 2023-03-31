package com.example.wetharpresnter.DataBase

import androidx.room.*
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


}