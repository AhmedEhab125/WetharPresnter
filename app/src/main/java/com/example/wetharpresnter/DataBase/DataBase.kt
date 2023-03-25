package com.example.wetharpresnter.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wetharpresnter.Models.WeatherData

class DataBase {
    @Database(entities = arrayOf(WeatherData::class), version = 1)
    abstract class ProductsDataBase : RoomDatabase() {
        abstract fun locations (): IDao

        companion object{
            @Volatile
            private var INSTANCE : ProductsDataBase?=null
            fun getInstance (context: Context): ProductsDataBase {
                return INSTANCE ?: synchronized(this){
                    var instance = Room.databaseBuilder(
                        context.applicationContext, ProductsDataBase::class.java ,"Locations").build()
                    INSTANCE =instance
                    instance
                }
            }
        }
    }
}