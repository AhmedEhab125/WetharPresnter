package com.example.wetharpresnter.View.Alert

import android.app.Dialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.RemoteException
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.RecyclerView
import com.example.wetharpresnter.Models.AlertDBModel
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.R
import com.example.wetharpresnter.ViewModel.AlertViewModel.AlertViewModel
import com.example.wetharpresnter.databinding.AlertItemBinding
import java.io.IOException

class AlertAdapter(
    var list: ArrayList<AlertDBModel>,
    var viewModelProvider: AlertViewModel
) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {
    lateinit var binding: AlertItemBinding
    lateinit var dialog: Dialog


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        binding = AlertItemBinding.inflate(layoutInflater, parent, false)


        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        initDialogConfirmation(binding.root.context)
        holder.binding.tvLocName.text = countryName(list.get(position).lat, list.get(position).lon)
        holder.binding.tvStartDate.text = list.get(position).fromDate
        holder.binding.tvEndDate.text = list.get(position).toDate

        holder.binding.btnDeleteFromAlert.setOnClickListener {
            dialog.show()
        }
        dialog.findViewById<Button>(R.id.btn_delete).setOnClickListener {
            viewModelProvider.deleteAlert(list.get(position))
            list.remove(list.get(position))
            notifyDataSetChanged()
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun countryName(lat: Double, lon: Double): String {
        var address = ""
        try {
            var geoCoder: Geocoder = Geocoder(binding.root.context)
            var addressList = arrayListOf<Address>()
            addressList = geoCoder.getFromLocation(lat, lon, 1) as ArrayList<Address>
            if (addressList.size > 0) {
                address = addressList.get(0).countryName
            }
        } catch (e: IOException) {
            Toast.makeText(binding.root.context, "cant get Area name", Toast.LENGTH_LONG).show()
        } catch (e: RemoteException) {
            Toast.makeText(binding.root.context, "cant get Area name", Toast.LENGTH_LONG).show()

        }

        return address
    }

    fun initDialogConfirmation(context: Context) {
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_iteam)
        val window: Window? = dialog.getWindow()
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(android.R.color.transparent)

    }

    class ViewHolder(var binding: AlertItemBinding) : RecyclerView.ViewHolder(binding.root)
}