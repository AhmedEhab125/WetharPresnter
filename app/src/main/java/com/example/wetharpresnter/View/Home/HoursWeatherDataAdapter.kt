package com.example.wetharpresnter.View.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wetharpresnter.Models.Hourly
import com.example.wetharpresnter.databinding.WeatherByHourIteamBinding

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
        holder.binding.tvTime.text=position.toString()
        holder.binding.tvHourTemp.text=list.get(position).temp.toString()
        var uri ="https://openweathermap.org/img/wn/${list.get(position).weather.get(0).icon}@2x.png"
        Glide.with(binding.root).load(uri).into(binding.ivHourWeatherState)
    }

    class ViewHolder(var binding: WeatherByHourIteamBinding) : RecyclerView.ViewHolder(binding.root)

}