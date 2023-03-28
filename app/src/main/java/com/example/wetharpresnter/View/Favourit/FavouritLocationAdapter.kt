package com.example.wetharpresnter.View.Favourit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.View.Home.DaysWeatherDataAdapter
import com.example.wetharpresnter.databinding.FavouritLocationIteamBinding
import com.example.wetharpresnter.databinding.WeatherByDayIteamBinding

class FavouritLocationAdapter(var list: ArrayList<WeatherData>) :
    RecyclerView.Adapter<FavouritLocationAdapter.ViewHolder>() {
    lateinit var binding: FavouritLocationIteamBinding
    fun setFavList( list:ArrayList<WeatherData>){
        this.list=list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        binding = FavouritLocationIteamBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvLocName.text = list.get(position).timezone
        holder.binding.tvLocTemp.text = Math.ceil(list.get(position).current?.temp ?: 0.0).toString()
        var uri =
            "https://openweathermap.org/img/wn/${list.get(position).current?.weather?.get(0)?.icon}@2x.png"
        Glide.with(binding.root).load(uri).into(binding.ivLocation)
    }

    class ViewHolder(var binding: FavouritLocationIteamBinding) :
        RecyclerView.ViewHolder(binding.root)

}