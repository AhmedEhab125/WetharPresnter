package com.example.wetharpresnter.View.Home

import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wetharpresnter.Utils.Constants
import com.example.wetharpresnter.Models.Daily
import com.example.wetharpresnter.databinding.WeatherByDayIteamBinding
import java.util.*

class DaysWeatherDataAdapter(var list: List<Daily> ,var configrations: SharedPreferences) :
    RecyclerView.Adapter<DaysWeatherDataAdapter.ViewHolder>() {
    lateinit var binding: WeatherByDayIteamBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        binding = WeatherByDayIteamBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size-1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        var simpleDate = SimpleDateFormat("dd-M-yyyy")
        var currentDate = simpleDate.format(list.get(position+1).dt?.times(1000L))

        var date: Date = simpleDate.parse(currentDate)
        Log.i("TAG", currentDate)
        var outFormat = SimpleDateFormat("EEEE",Locale(configrations.getString(Constants.LANG, "")))
        var goal = outFormat.format(date)

        holder.binding.tvDiscription.text = list.get(position).weather.get(0).description

        holder.binding.tvDay.text = goal
        var maxTemp =Math.ceil(list.get(position).temp?.max ?: 0.0).toInt()
        var minTemp =Math.ceil(list.get(position).temp?.min ?: 0.0).toInt()
        holder.binding.tvMinTemp.text = maxTemp.toString()+"/"+tempFormat(minTemp.toString())
        var uri =
            "https://openweathermap.org/img/wn/${list.get(position).weather.get(0).icon}@2x.png"
        Glide.with(binding.root).load(uri).into(binding.ivDayState)
    }
    fun tempFormat(temp: String): String {
        var format = ""
        if (configrations.getString(Constants.UNITS, "").equals(Constants.DEFAULT)) {
            format = "$temp°K"

        } else if (configrations.getString(Constants.UNITS, "").equals(Constants.METRIC)) {
            format = "$temp°C"

        } else if (configrations.getString(Constants.UNITS, "").equals(Constants.IMPERIAL)) {
            format = "$temp°F"
        }
        return format
    }

    class ViewHolder(var binding: WeatherByDayIteamBinding) : RecyclerView.ViewHolder(binding.root)

}