package com.zkrallah.z_pomodoro.model


import java.time.LocalDateTime

data class Alarm(
    val time: LocalDateTime,
    val message: String
)