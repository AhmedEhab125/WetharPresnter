package com.example.wetharpresnter.Models


data class Daily(

    var dt: Double? = null,
    var sunrise: Double? = null,
    var sunset: Double? = null,
    var moonrise: Double? = null,
    var moonset: Double? = null,
    var moonPhase: Double? = null,
    var temp: Temp? = Temp(),
    var feelsLike: FeelsLike? = FeelsLike(),
    var pressure: Double? = null,
    var humidity: Double? = null,
    var dewPoint: Double? = null,
    var windSpeed: Double? = null,
    var windDeg: Double? = null,
    var windGust: Double? = null,
    var weather: ArrayList<Weather> = arrayListOf(),
    var clouds: Double? = null,
    var pop: Double? = null,
    var rain: Double? = null,
    var snow: Double? = null,
    var uvi: Double? = null

)
