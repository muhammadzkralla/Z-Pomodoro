package com.zkrallah.z_pomodoro.alarm

import android.R
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.zkrallah.z_pomodoro.MainActivity
import com.zkrallah.z_pomodoro.NotificationService
import java.time.LocalDateTime
import java.time.ZoneId

class AndroidAlarmScheduler : Service() {


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // Getting the extras from the intent.
        val extra = intent?.getStringExtra("time")
        val endTime = LocalDateTime.parse(extra)
        val time = endTime.atZone(ZoneId.systemDefault()).toEpochSecond()
            .minus(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()) * 1000

        // Apply the pending intent to launch an action once clicked on
        val activityIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this
            , 1
            , activityIntent
            , PendingIntent.FLAG_IMMUTABLE
        )

        // This is the Notification object itself, this notification belongs to the specified channel id
        val notification = NotificationCompat.Builder(this, NotificationService.CHANNEL_ID)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setContentTitle("Pomodoro")
            .setContentText("the timer is ${(time / 1000) / 60}:${(time / 1000) % 60}")
            .setContentIntent(pendingIntent)
            .build()

        // Start the Foreground Service
        startForeground(1, notification)

        // Start the timer with passed time
        object: CountDownTimer(time, 1000){
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                // Once the Timer is done, send a Notification and close the service
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                // Apply the pending intent to launch an action once clicked on
                val activity = Intent(this@AndroidAlarmScheduler, MainActivity::class.java)
                val pending = PendingIntent.getActivity(
                    this@AndroidAlarmScheduler
                    , 1
                    , activity
                    , PendingIntent.FLAG_IMMUTABLE
                )

                // This is the Notification object itself, this notification belongs to the specified channel id
                val notificationDone = NotificationCompat.Builder(this@AndroidAlarmScheduler, NotificationService.CHANNEL_ID)
                    .setSmallIcon(R.drawable.sym_def_app_icon)
                    .setContentTitle("Pomodoro")
                    .setContentText("the timer is DONE")
                    .setContentIntent(pending)
                    .build()

                notificationManager.notify(3, notificationDone)
                onDestroy()
                stopSelf()
            }

        }.start()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}