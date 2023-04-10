package com.example.wetharpresnter.ViewModel.AlertViewModel

import android.Manifest
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.Models.AlertDBModel
import com.example.wetharpresnter.Models.WeatherData
import com.example.wetharpresnter.Netwoek.ApiState
import com.example.wetharpresnter.R
import com.example.wetharpresnter.Repo.Repository
import com.example.wetharpresnter.View.MainActivity.MainActivity
import kotlinx.coroutines.*
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat


class AlarmRecever() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var id = intent?.getIntExtra("id", -1);
        var lat = intent?.getDoubleExtra("lat", 0.0);
        var lon = intent?.getDoubleExtra("lon", 0.0);
        var state = intent?.getStringExtra("state");
        var endDate=intent?.getStringExtra("endDate")
        println("d5aaaaaaaaaaaal"+ state)



        CoroutineScope(Dispatchers.Main).launch {
            if (state != null) {
                if (context != null) {
                    var data =getData(id.toString(),state,context, lat?.toDouble() ?: 0.0, lon?.toDouble() ?: 0.0,AlertViewModel(context,Repository),endDate)


                }
            }
        }


    }

    fun generateUniqueIntValue(a: Long, b: Long, str: String, strType: String): Int {
        val input = "$a$b$str$strType"
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))
        val truncatedHash = hash.copyOfRange(0, 4) // Truncate hash to 4 bytes
        return truncatedHash.fold(0) { acc, byte -> (acc shl 8) + (byte.toInt() and 0xff) }
    }

    fun generateNotification(context: Context, id: String, weatherData: WeatherData) {
        var alertData=context.getString(R.string.alert_msg)
        if (weatherData.alerts.size>0){
            alertData=""
            for (i in weatherData.alerts){
                alertData+=i.description +"\n"
            }
        }

        var builder = NotificationCompat.Builder(context!!, id)
            .setSmallIcon(R.drawable.sunny)
            .setContentText("notificationContent")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(alertData)
            ).apply {
                val resultIntent = Intent(context, MainActivity::class.java)
                val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                    // Add the intent, which inflates the back stack
                    addNextIntentWithParentStack(resultIntent)
                    // Get the PendingIntent containing the entire back stack
                    getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                }
                setContentIntent(resultPendingIntent)
            }.setDefaults(NotificationCompat.DEFAULT_SOUND)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        var notificationManagerCompat = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManagerCompat.notify(1, builder.build())
    }


    private suspend fun alarm(context: Context,weatherData: WeatherData) {
        var alertData=context.getString(R.string.alert_msg)
        if (weatherData.alerts.size>0){
            alertData=""
            for (i in weatherData.alerts){
                alertData+=i.description +"\n"
            }
        }

        var LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            WindowManager.LayoutParams.TYPE_PHONE

        val mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)

        val view: View =
            LayoutInflater.from(context).inflate(R.layout.over_app_window, null, false)
        view.findViewById<Button>(R.id.btn_close_alarm).setOnClickListener {
            mediaPlayer.release()
        }
        view.findViewById<TextView>(R.id.tv_alarm_discription).text=alertData
         val dismissBtn = view.findViewById(R.id.btn_close_alarm) as Button
       //  val textView = view.findViewById(R.id.descriptionAlarm) as TextView
        val layoutParams =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        layoutParams.gravity = Gravity.CENTER

        val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager

        withContext(Dispatchers.Main) {
            windowManager.addView(view, layoutParams)
            view.visibility = View.VISIBLE
            // textView.text = message
        }

        mediaPlayer.start()
        mediaPlayer.isLooping = true
         dismissBtn.setOnClickListener {
             mediaPlayer?.release()
             windowManager.removeView(view)
         }

    }

    suspend fun getData(
        id: String, state: String,
        context: Context,
        lat: Double,
        lon: Double,
        alertViewModel: AlertViewModel,
        endDate: String?
    ): WeatherData {
        val lang = context.getSharedPreferences("Configuration", Context.MODE_PRIVATE)
            .getString(Constants.LANG, "")
        alertViewModel.getWeatherDataFromApi(lat.toString(), lon.toString(), lang!!)
        var data = WeatherData()
        alertViewModel.accessList.collect() { result ->
            when (result) {
                is ApiState.Success -> {
                    val curentTime = System.currentTimeMillis();
                    data = result.date!!
                    if (state.equals(Constants.NOTIFICATIONS)) {
                         generateNotification(context!!, id,data)

                    } else {
                        alarm(context!!,data)
                    }
                    if ( SimpleDateFormat("dd-M-yyyy").format(curentTime).equals(endDate)){
                        println(id+" froom recevr")
                        alertViewModel.deleteAlertByID(id.toInt())
                    }
                 //   generateNotification(context!!, id.toString(),data)
                }
                else -> {

                }
            }

        }
        return data
    }


}