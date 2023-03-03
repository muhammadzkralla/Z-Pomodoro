package com.zkrallah.z_pomodoro.alarm

import com.zkrallah.z_pomodoro.model.Alarm

interface AlarmScheduler {
    fun schedule(item: Alarm)
    fun cancel(item: Alarm)
}