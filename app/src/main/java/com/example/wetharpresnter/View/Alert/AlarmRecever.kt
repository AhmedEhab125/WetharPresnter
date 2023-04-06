package com.example.wetharpresnter.View.Alert

import android.Manifest
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wetharpresnter.Constants
import com.example.wetharpresnter.R
import com.example.wetharpresnter.View.MainActivity.MainActivity
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class AlarmRecever(var chanelId:String) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var builder = NotificationCompat.Builder(context!!,chanelId)
            .setSmallIcon(R.drawable.sunny)
            .setContentTitle("Wethear2Day")
            .setContentText("notificationContent")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line...")
            ).apply {
                val resultIntent= Intent(context, MainActivity::class.java)
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
        var notificationManagerCompat= NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManagerCompat.notify(1,builder.build())

    }
    fun generateUniqueIntValue(a: Long, b: Long, str: String, strType:String): Int {
        val input = "$a$b$str$strType"
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))
        val truncatedHash = hash.copyOfRange(0, 4) // Truncate hash to 4 bytes
        return truncatedHash.fold(0) { acc, byte -> (acc shl 8) + (byte.toInt() and 0xff) }
    }
}