package com.zkrallah.z_pomodoro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zkrallah.z_pomodoro.service.PomodoroScheduler
import com.zkrallah.z_pomodoro.databinding.ActivityMainBinding
import java.time.LocalDateTime


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener {
            val duration = binding.edtTimer.text.toString().toLong()
            val serviceIntent = Intent(this, PomodoroScheduler::class.java).apply {
                putExtra("time",
                    (LocalDateTime.now().plusSeconds(duration)).toString())
            }
            startForegroundService(serviceIntent)
        }
        binding.cancelBtn.setOnClickListener {
            val serviceIntent = Intent(this, PomodoroScheduler::class.java)
            stopService(serviceIntent)
            binding.timeLeft.text = "0:0"
        }
    }
}