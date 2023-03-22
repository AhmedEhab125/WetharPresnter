package com.example.wetharpresnter.Netwoek.Repo

import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Netwoek.WeatherService

class Repository {
    companion object{
        fun getWetharData(lat :String,lon :String ,lang: String="en"): WeatherData? {
            return WeatherService.getWetharData(lat, lon, lang)
        }
    }
}