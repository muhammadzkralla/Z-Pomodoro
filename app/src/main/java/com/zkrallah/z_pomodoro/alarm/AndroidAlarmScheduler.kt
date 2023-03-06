package com.zkrallah.z_pomodoro.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.zkrallah.z_pomodoro.model.Alarm
import com.zkrallah.z_pomodoro.receiver.AlarmReceiver
import java.time.LocalDateTime
import java.time.ZoneId

class AndroidAlarmScheduler(
    private val context: Context
): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    var timeLeft = 0L

    @SuppressLint("MissingPermission")
    override fun schedule(item: Alarm) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", item.message)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        timeLeft = item.time.atZone(ZoneId.systemDefault()).toEpochSecond()
            .minus(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()) * 1000
    }

    override fun cancel(item: Alarm) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}