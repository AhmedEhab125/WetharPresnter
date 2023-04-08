package com.example.wetharpresnter.View.Alert

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.R
import com.example.wetharpresnter.View.MainActivity.MainActivity
import kotlinx.coroutines.*
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


class AlarmRecever() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      var id =  intent?.getIntExtra("id",-1)


        CoroutineScope(Dispatchers.Main).launch {
            generateNotification(context!!,id.toString())
        }


    }

    fun generateUniqueIntValue(a: Long, b: Long, str: String, strType: String): Int {
        val input = "$a$b$str$strType"
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))
        val truncatedHash = hash.copyOfRange(0, 4) // Truncate hash to 4 bytes
        return truncatedHash.fold(0) { acc, byte -> (acc shl 8) + (byte.toInt() and 0xff) }
    }

    fun generateNotification(context: Context,id :String) {

        var builder = NotificationCompat.Builder(context!!,id)
            .setSmallIcon(R.drawable.sunny)
            .setContentTitle("Wethear2Day")
            .setContentText("notificationContent")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line...")
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


    private suspend fun alarm(context: Context) {

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
        /* val dismissBtn = view.findViewById(R.id.btnDismissAlarm) as Button
         val textView = view.findViewById(R.id.descriptionAlarm) as TextView*/
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
        /* dismissBtn.setOnClickListener {
             mediaPlayer?.release()
             windowManager.removeView(view)
         }
         repository.deleteAlert(entityAlert)*/
    }


}