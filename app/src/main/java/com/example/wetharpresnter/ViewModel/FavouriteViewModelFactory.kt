package com.example.wetharpresnter.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wetharpresnter.ViewModel.HomeViewModel.WeatherViewModel


class FavouriteViewModelFactory(var context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            FavouriteViewModel(context) as T
        } else {
            throw java.lang.IllegalArgumentException("Cant cast class ")
        }
    }
}