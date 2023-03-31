package com.example.wetharpresnter

import android.content.Context
import android.net.ConnectivityManager

object NetworkListener {
    fun getConnectivity(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null
    }
}