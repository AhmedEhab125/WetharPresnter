package com.example.wetharpresnter.Models

import androidx.room.Embedded

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Alerts")
data class AlertDBModel(
    @PrimaryKey
    var ID:Int,
    var lat: Double,
    var lon: Double,
    var fromTime:Long,
    var fromDate:String,
    var toTime:Long,
    var toDate:String,
    var state :String



) {


    constructor() : this(
        -1,
        0.0,
        0.0,
        1L,
        "",
        1L,
        "",
        ""

    )

}