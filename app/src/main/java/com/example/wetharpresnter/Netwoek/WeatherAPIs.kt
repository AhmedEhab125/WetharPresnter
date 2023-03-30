package com.example.wetharpresnter.Netwoek

import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.Models.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIs {


    @GET("/data/2.5/onecall?")
    fun getWetharData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") lang: String,
        @Query("appid") appId: String = "079fa9942e78463cda08c0bbceceffd9",
        @Query("units") units: String = Constants.DEFAULT

        // Resources.getSystem().getString(R.string.API_KEY)
    ): Call<WeatherData>
}