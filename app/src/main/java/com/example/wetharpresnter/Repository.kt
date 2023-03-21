package com.example.wetharpresnter

import com.example.wetharpresnter.Models.WetharData
import com.example.wetharpresnter.Netwoek.WetharService

class Repository {
    private var wethar = mutableListOf<WetharData>()
    companion object{

        fun getWetharData(lat :String,lon :String ,lang: String="en"): WetharData? {
            return WetharService.getWetharData(lat, lon, lang)
        }
    }
}