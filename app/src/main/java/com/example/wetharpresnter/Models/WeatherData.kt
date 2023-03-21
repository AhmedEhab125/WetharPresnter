package com.example.wetharpresnter.Models

data class WeatherData(
    var lat: Double? = null,
    var lon: Double? = null,
    var timezone: String? = null,
    var timezoneOffset: Int? = null,
    var current: Current? = Current(),
    var minutely: ArrayList<Minutely> = arrayListOf(),
    var hourly: ArrayList<Hourly> = arrayListOf(),
    var daily: ArrayList<Daily> = arrayListOf(),
    var alerts: ArrayList<Alerts> = arrayListOf()

)


