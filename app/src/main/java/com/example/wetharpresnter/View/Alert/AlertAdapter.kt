package com.example.wetharpresnter.View.Alert

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.ViewModel.AlertViewModel.AlertViewModel
import com.example.wetharpresnter.databinding.AlertItemBinding

class AlertAdapter(
    var list: ArrayList<WeatherData>,
    var viewModelProvider: AlertViewModel
) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {
    lateinit var binding:AlertItemBinding



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        binding = AlertItemBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       binding.tvLocName.text=list.get(position).timezone
        binding.btnDeleteFromAlert.setOnClickListener {

        }
    }
    class ViewHolder(binding: AlertItemBinding) :RecyclerView.ViewHolder(binding.root)
}