package com.example.wetharpresnter.Netwoek

import com.example.wetharpresnter.Models.WetharData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WetharService {

    private val apiInstance = Retrofit.Builder().baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val service = apiInstance.create(WetharAPIs::class.java)

    fun getWetharData(lat :String,lon :String ,lang: String): WetharData? {
        var data = service.getWetharData(lat,lon,lang).execute().body()
        return data
    }
}
