package com.example.wetharpresnter.Models


data class Hourly(

    var dt: Int? = null,
    var temp: Double? = null,
    var feelsLike: Double? = null,
    var pressure: Double? = null,
    var humidity: Double? = null,
    var dewPoint: Double? = null,
    var uvi: Double? = null,
    var clouds: Int? = null,
    var visibility: Int? = null,
    var windSpeed: Double? = null,
    var windDeg: Double? = null,
    var windGust: Double? = null,
    var weather: ArrayList<Weather> = arrayListOf(),
    var pop: Double? = null

)