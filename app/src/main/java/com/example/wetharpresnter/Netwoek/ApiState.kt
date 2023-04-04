package com.example.wetharpresnter.Netwoek

import com.example.wetharpresnter.Models.WeatherData

sealed class ApiState {
    class Success(val date:WeatherData?) : ApiState()
    class Failure (val msg :Throwable) :ApiState()
    object Loading:ApiState()
}