package com.zkrallah.z_pomodoro

import android.R
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationService(private val context: Context) {

    // Create the notification manager instance
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(){
        // Apply the pending intent to launch an action once clicked on
        val activityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context
            , 1
            , activityIntent
            , PendingIntent.FLAG_IMMUTABLE
        )

        // This is the Notification object itself, this notification belongs to the specified channel id
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setContentTitle("Pomodoro")
            .setContentText("the timer is DONE")
            .setContentIntent(pendingIntent)
            .build()

        // Shows the notifications
        notificationManager.notify(1, notification)

    }
    companion object{
        const val CHANNEL_ID = "counter_channel"
    }

}