package com.example.wetharpresnter.Models

data class Alerts(

    var senderName: String? = null,
    var event: String? = null,
    var start: Int? = null,
    var end: Int? = null,
    var description: String? = null,
    var tags: ArrayList<String> = arrayListOf()

)