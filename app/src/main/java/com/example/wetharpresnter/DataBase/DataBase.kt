package com.example.wetharpresnter.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wetharpresnter.Models.WeatherData

class DataBase {
    @Database(entities = arrayOf(WeatherData::class), version = 4)
    @TypeConverters(TypeConverter::class)

    abstract class LocationDataBase : RoomDatabase() {
        abstract fun locations (): IDao

        companion object{
            @Volatile
            private var INSTANCE : LocationDataBase?=null
            fun getInstance (context: Context): LocationDataBase {
                return INSTANCE ?: synchronized(this){
                    var instance = Room.databaseBuilder(
                        context.applicationContext, LocationDataBase::class.java ,"Locations").build()
                    INSTANCE =instance
                    instance
                }
            }
        }
    }
}