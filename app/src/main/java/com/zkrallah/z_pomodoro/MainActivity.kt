package com.zkrallah.z_pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zkrallah.z_pomodoro.alarm.AndroidAlarmScheduler
import com.zkrallah.z_pomodoro.databinding.ActivityMainBinding
import com.zkrallah.z_pomodoro.model.Alarm
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val scheduler = AndroidAlarmScheduler(this)
        var alarmItem: Alarm? = null

        binding.startBtn.setOnClickListener {
            val duration = binding.edtTimer.text.toString().toLong()
            alarmItem = Alarm(LocalDateTime.now().plusSeconds(duration), duration.toString())
            scheduler.schedule(alarmItem!!)
        }
        binding.cancelBtn.setOnClickListener {
            scheduler.cancel(alarmItem!!)
        }
    }
}