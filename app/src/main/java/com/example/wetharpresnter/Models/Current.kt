package com.example.wetharpresnter.Models

data class Current(

    var dt: Double? = null,
    var sunrise: Double? = null,
    var sunset: Double? = null,
    var temp: Double? = null,
    var feelsLike: Double? = null,
    var pressure: Double? = null,
    var humidity: Double? = null,
    var dewPoint: Double? = null,
    var uvi: Double? = null,
    var clouds: Double? = null,
    var visibility: Double? = null,
    var windSpeed: Double? = null,
    var windDeg: Double? = null,
    var windGust: Double? = null,
    var weather: ArrayList<Weather> = arrayListOf()

)
