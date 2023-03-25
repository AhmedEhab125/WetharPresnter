package com.example.wetharpresnter.View.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wetharpresnter.Models.Hourly
import com.example.wetharpresnter.databinding.WeatherByHourIteamBinding
import java.text.SimpleDateFormat
import java.util.*

class HoursWeatherDataAdapter(var list: List<Hourly>) :
    RecyclerView.Adapter<HoursWeatherDataAdapter.ViewHolder>() {
    lateinit var binding: WeatherByHourIteamBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        binding = WeatherByHourIteamBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var date = Date(list.get(position).dt?.times(1000L) ?: 0)
        var sdf= SimpleDateFormat("hh:mm a")
        sdf.timeZone = TimeZone.getDefault()
        var formatedData=sdf.format(date)
        holder.binding.tvTime.text=formatedData.toString()
        holder.binding.tvHourTemp.text=Math.ceil(list.get(position).temp?:0.0).toInt().toString()+"°C"
        var uri ="https://openweathermap.org/img/wn/${list.get(position).weather.get(0).icon}@2x.png"
        Glide.with(binding.root).load(uri).into(binding.ivHourWeatherState)
    }

    class ViewHolder(var binding: WeatherByHourIteamBinding) : RecyclerView.ViewHolder(binding.root)

}