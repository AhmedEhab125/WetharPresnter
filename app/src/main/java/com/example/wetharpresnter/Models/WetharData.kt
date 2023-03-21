package com.example.wetharpresnter.Models

data class WetharData(

    var coord: Coord? = Coord(),
    var weather: ArrayList<Weather> = arrayListOf(),
    var base: String? = null,
    var main: Main? = Main(),
    var visibility: Int? = null,
    var wind: Wind? = Wind(),
    var clouds: Clouds? = Clouds(),
    var dt: Int? = null,
    var sys: Sys? = Sys(),
    var timezone: Int? = null,
    var id: Int? = null,
    var name: String? = null,
    var cod: Int? = null

) 