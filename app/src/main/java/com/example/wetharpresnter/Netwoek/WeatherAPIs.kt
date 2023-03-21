package com.example.wetharpresnter.Netwoek
import com.example.wetharpresnter.Models.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIs {

    @GET("/data/2.5/weather?")
    fun getWetharData(@Query("lat") lat :String,@Query("lon") lon :String,
                      @Query("lang") lang: String,
                      @Query("appid") appId:String= "d6a347a6872fc132aad35e4ed9e1f138"
                         // Resources.getSystem().getString(R.string.API_KEY)
    ) :Call<WeatherData>
}