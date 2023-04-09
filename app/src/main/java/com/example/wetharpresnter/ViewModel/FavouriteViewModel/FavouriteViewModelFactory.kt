package com.example.wetharpresnter.ViewModel.FavouriteViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wetharpresnter.Repo.IRepo


class FavouriteViewModelFactory(var context: Context,var iRepo: IRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            FavouriteViewModel(context,iRepo) as T
        } else {
            throw java.lang.IllegalArgumentException("Cant cast class ")
        }
    }
}