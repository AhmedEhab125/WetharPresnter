package com.example.wetharpresnter.ViewModel.HomeViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wetharpresnter.Repo.Repository

class ViewModelFactory(var context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(context,Repository) as T
        } else {
            throw java.lang.IllegalArgumentException("Cant cast class ")
        }
    }
}
