package com.example.wetharpresnter.ViewModel.FavouriteViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class FavouriteViewModelFactory(var context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            FavouriteViewModel(context) as T
        } else {
            throw java.lang.IllegalArgumentException("Cant cast class ")
        }
    }
}