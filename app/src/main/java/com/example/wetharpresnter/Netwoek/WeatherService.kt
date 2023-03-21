package com.example.wetharpresnter.Netwoek

import com.example.wetharpresnter.Models.WeatherData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherService {

    private val apiInstance = Retrofit.Builder().baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val service = apiInstance.create(WeatherAPIs::class.java)

    fun getWetharData(lat :String,lon :String ,lang: String): WeatherData? {
        var data = service.getWetharData(lat,lon,lang).execute().body()
        return data
    }
}
