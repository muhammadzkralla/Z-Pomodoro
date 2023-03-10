package com.zkrallah.z_pomodoro

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zkrallah.z_pomodoro.databinding.ActivityMainBinding
import com.zkrallah.z_pomodoro.service.PomodoroScheduler
import java.time.LocalDateTime
import java.time.ZoneId


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: SharedPreferences
    private var isTimerActive: Boolean = false
    private var counter: CountDownTimer? = null
    private var duration = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences("prefs", MODE_PRIVATE)

        binding.pomodoroBtn.setOnClickListener {
            binding.timeLeft.text = "25:00"
            duration = 1500L
        }

        binding.breakBtn.setOnClickListener {
            binding.timeLeft.text = "5:00"
            duration = 300L
        }

        binding.startBtn.setOnClickListener {
            if (duration != 0L) {
                val serviceIntent = Intent(this, PomodoroScheduler::class.java).apply {
                    putExtra(
                        "time",
                        (LocalDateTime.now().plusSeconds(duration)).toString()
                    )
                }
                startForegroundService(serviceIntent)
                val editor = preferences.edit()
                editor.putBoolean("timer", true)
                editor.putLong(
                    "endDate", LocalDateTime.now()
                        .plusSeconds(duration)
                        .atZone(ZoneId.systemDefault()).toEpochSecond()
                )
                editor.putLong("type", duration)
                editor.apply()
                startCounterUI(
                    LocalDateTime.now()
                        .plusSeconds(duration)
                        .atZone(ZoneId.systemDefault()).toEpochSecond(),
                    duration
                )
            }
        }
        binding.cancelBtn.setOnClickListener {
            val serviceIntent = Intent(this, PomodoroScheduler::class.java)
            stopService(serviceIntent)
            binding.timeLeft.text = "00:00"
            val editor = preferences.edit()
            editor.putBoolean("timer", false)
            editor.putLong(
                "endDate",
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
            )
            editor.putLong("type", 0L)
            editor.apply()
            counter?.cancel()
            duration = 0L
            binding.progressBar.progress = 100
        }
    }

    private fun startCounterUI(endDate: Long, type: Long) {
        val time = endDate
            .minus(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()) * 1000
        Toast.makeText(this, time.div(1000).toString(), Toast.LENGTH_SHORT).show()
        counter = object : CountDownTimer(time, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.timeLeft.text = "$minutes:$seconds"
                binding.progressBar.progress =
                    millisUntilFinished.toDouble().div(type.times(1000).toDouble()).times(100).toInt()
            }

            override fun onFinish() {
                val editor = preferences.edit()
                editor.putBoolean("timer", false)
                editor.putLong(
                    "endDate",
                    LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
                )
                editor.putLong("type", 0L)
                editor.apply()
                duration = 0L
            }

        }
        counter!!.start()
    }

    override fun onStart() {
        isTimerActive = preferences.getBoolean("timer", false)
        if (isTimerActive) {
            val endDate = preferences.getLong(
                "endDate",
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
            )
            if (endDate <= LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()) {
                val editor = preferences.edit()
                editor.putBoolean("timer", false)
                editor.putLong(
                    "endDate",
                    LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
                )
                editor.putLong("type", 0L)
                editor.apply()
                isTimerActive = false
                duration = 0L
            } else {
                val type = preferences.getLong("type", 0L)
                startCounterUI(endDate, type)
            }
        }
        super.onStart()
    }
}