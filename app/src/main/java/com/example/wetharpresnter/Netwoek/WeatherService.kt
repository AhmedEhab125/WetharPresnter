package com.example.wetharpresnter.Netwoek

import com.example.wetharpresnter.Models.WeatherData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherService {

    private val apiInstance = Retrofit.Builder().baseUrl("https://pro.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val service = apiInstance.create(WeatherAPIs::class.java)

    fun getWetharData(lat :String,lon :String ,lang: String,unit :String): WeatherData? {
        var data = service.getWetharData(lat,lon,lang,unit).execute().body()
        return data
    }
}
