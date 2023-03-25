package com.example.wetharpresnter.Models

import androidx.room.Ignore

data class Current(
    var clouds: Int,//need
    var dew_point: Double,
    var dt: Int,//need
    var feels_like: Double,
    var humidity: Int,//need
    var pressure: Int,//need
    @Ignore
    var rain:Rain?,
    var sunrise: Int,
    var sunset: Int,
    var temp: Double,//need
    var uvi: Double,
    var visibility: Int,
    var weather: ArrayList<Weather>,
    var wind_deg: Int,
    var wind_speed: Double//need
)
{
    constructor()
            : this(0,0.0,0,0.0,0,0,null,0,0,0.0,0.0,0, arrayListOf(),0,0.0)

}

