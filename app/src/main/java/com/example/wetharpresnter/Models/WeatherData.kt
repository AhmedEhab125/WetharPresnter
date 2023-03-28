package com.example.wetharpresnter.Models

import androidx.room.*

@Entity(tableName = "Locations")
data class WeatherData(
    var lat: Double,
    var lon: Double,
    @PrimaryKey
    var timezone: String,
    var timezoneOffset: Int? = null,
    @Embedded
    var current: Current?,
    @Ignore
    var minutely: ArrayList<Minutely> = arrayListOf(),
    var hourly: ArrayList<Hourly> = arrayListOf(),
    var daily: ArrayList<Daily> = arrayListOf(),
    var alerts: ArrayList<Alerts> = arrayListOf(),


    ) {


    constructor() : this(
        0.0,
        0.0,
        "",
        0,
        null,
        arrayListOf(),
        arrayListOf(),
        arrayListOf(),
        arrayListOf()
    )

}


