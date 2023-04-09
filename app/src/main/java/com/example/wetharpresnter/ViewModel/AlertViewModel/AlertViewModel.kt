package com.example.wetharpresnter.ViewModel.AlertViewModel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.Models.AlertDBModel
import com.example.wetharpresnter.Netwoek.ApiState
import com.example.wetharpresnter.Repo.Repository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel(var context: Context) : ViewModel() {
    private var list: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    var accessList: StateFlow<ApiState> = list
    private var alertList = MutableLiveData<List<AlertDBModel>>()
    var accessAlertList = alertList


    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    fun getWeatherDataFromApi(
        lat: String,
        lon: String,
        lang: String = "en",
        unit: String = Constants.DEFAULT
    ) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

            Repository.getWetharData(lat, lon, lang, unit).catch { e ->
                list.value = ApiState.Failure(e)
            }.collect { data ->
                list.value = ApiState.Success(data)
            }
        }
    }

    fun addToAlert(
        lat: String,
        lon: String,
        id: Int,
        fromTime: Long,
        fromDate: String,
        toTime: Long,
        toDate: String,
        state: String
    ) {


        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

            Repository.insertAlert(
                context,
                AlertDBModel(
                    id,
                    lat.toDouble(),
                    lon.toDouble(),
                    fromTime,
                    fromDate,
                    toTime,
                    toDate,
                    state
                )
            )
            Repository.getAlerts(context).collect {
                alertList.postValue(it)
            }
        }

    }

    fun getAlerts() {

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            Log.i("done", "getFavLocations: ")
            Repository.getAlerts(context).collect {
                alertList.postValue(it)
            }
        }
    }


    private fun countryName(lat: Double, lon: Double): String {
        var address = ""
        var geoCoder: Geocoder = Geocoder(context)
        var addressList = arrayListOf<Address>()
        addressList = geoCoder.getFromLocation(lat, lon, 1) as ArrayList<Address>
        if (addressList.size > 0) {
            address = addressList.get(0).countryName
        }
        return address
    }

    fun deleteAlert(alertDBModel: AlertDBModel) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            Repository.deleteAlert(context, alertDBModel)
            cancleAlarm(alertDBModel.ID, alertDBModel)
        }
    }

    private fun cancleAlarm(alertId: Int, alertDBModel: AlertDBModel) {
        var startDate = alertDBModel.fromDate
        var startMonth = startDate.split("-").get(1)
        var startDay = startDate.split("-").get(0)

        var endDate = alertDBModel.fromDate
        var endMonth = endDate.split("-").get(1)
        var endDay = endDate.split("-").get(0)

        var interval =
            (endDay.toInt() - startDay.toInt()) + ((endMonth.toInt() - startMonth.toInt()) * 30)

        for (i in 0..interval) {
            var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmRecever::class.java)
            var pendingIntent = PendingIntent.getBroadcast(context, alertId + i, intent, 0)
            alarmManager.cancel(pendingIntent)
            Toast.makeText(context, "Alarm set Sucssesfuly ", Toast.LENGTH_LONG).show()
        }


    }
}