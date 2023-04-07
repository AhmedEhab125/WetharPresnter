package com.example.wetharpresnter.ViewModel.AlertViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wetharpresnter.ViewModel.WeatherViewModel

class AlertViewModelFactory(var context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            AlertViewModel(context) as T
        } else {
            throw java.lang.IllegalArgumentException("Cant cast class ")
        }
    }
}