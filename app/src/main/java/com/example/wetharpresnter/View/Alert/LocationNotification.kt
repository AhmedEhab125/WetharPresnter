package com.example.wetharpresnter.View.Alert

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.R
import com.example.wetharpresnter.View.MainActivity.MainActivity

class LocationNotification(val context: Context, notificationContent: String) {
    var builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
        .setSmallIcon(R.drawable.sunny)
        .setContentTitle("Wethear2Day")
        .setContentText(notificationContent)
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line...")
        ).apply {
            val resultIntent= Intent(context,MainActivity::class.java)
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                // Add the intent, which inflates the back stack
                addNextIntentWithParentStack(resultIntent)
                // Get the PendingIntent containing the entire back stack
                getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }
            setContentIntent(resultPendingIntent)
        }.setDefaults(NotificationCompat.DEFAULT_SOUND)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)


    fun createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance).apply {
                this.enableLights(true)
                this.enableVibration(true)
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                }
                notify(1, builder.build())
            }

        }
    }



}

