package com.example.wetharpresnter

import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Netwoek.WeatherService

class Repository {
    private var wethar = mutableListOf<WeatherData>()
    companion object{

        fun getWetharData(lat :String,lon :String ,lang: String="en"): WeatherData? {
            return WeatherService.getWetharData(lat, lon, lang)
        }
    }
}