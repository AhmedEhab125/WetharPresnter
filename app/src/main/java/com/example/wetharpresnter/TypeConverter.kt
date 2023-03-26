package com.example.wetharpresnter

import androidx.room.TypeConverter
import com.example.wetharpresnter.Models.Alerts
import com.example.wetharpresnter.Models.Daily
import com.example.wetharpresnter.Models.Hourly
import com.example.wetharpresnter.Models.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.collections.ArrayList

class TypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): ArrayList<Hourly> {
        if (data == null) {
            return arrayListOf()
        }
        val listType: Type = object : TypeToken<ArrayList<Hourly?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: ArrayList<Hourly?>?): String {
        return gson.toJson(someObjects)
    }

    @TypeConverter
    fun stringToSomeObjectListHour(data: String?): ArrayList<Daily> {
        if (data == null) {
            return arrayListOf()
        }
        val listType: Type = object : TypeToken<ArrayList<Daily?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToStringHour(someObjects: ArrayList<Daily?>?): String {
        return gson.toJson(someObjects)
    }
    @TypeConverter
    fun stringToSomeObjectListAlerts(data: String?): ArrayList<Alerts> {
        if (data == null) {
            return arrayListOf()
        }
        val listType: Type = object : TypeToken<ArrayList<Alerts?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToStringAlerts(someObjects: ArrayList<Alerts?>?): String {
        return gson.toJson(someObjects)
    }
    @TypeConverter
    fun stringToSomeObjectListWeathar(data: String?): ArrayList<Weather> {
        if (data == null) {
            return arrayListOf()
        }
        val listType: Type = object : TypeToken<ArrayList<Weather?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToStringWeather(someObjects: ArrayList<Weather?>?): String {
        return gson.toJson(someObjects)
    }

}