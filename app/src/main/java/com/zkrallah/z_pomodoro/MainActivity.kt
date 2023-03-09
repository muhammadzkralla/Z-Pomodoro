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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences("prefs", MODE_PRIVATE)

        binding.startBtn.setOnClickListener {
            val duration = binding.edtTimer.text.toString().toLong()
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
            editor.apply()
            startCounterUI(LocalDateTime.now()
                .plusSeconds(duration)
                .atZone(ZoneId.systemDefault()).toEpochSecond())
        }
        binding.cancelBtn.setOnClickListener {
            val serviceIntent = Intent(this, PomodoroScheduler::class.java)
            stopService(serviceIntent)
            binding.timeLeft.text = "0:0"
            val editor = preferences.edit()
            editor.putBoolean("timer", false)
            editor.putLong(
                "endDate",
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
            )
            editor.apply()
            counter?.cancel()
        }
    }

    private fun startCounterUI(endDate: Long) {
        val time = endDate
            .minus(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()) * 1000
        Toast.makeText(this, time.toString(), Toast.LENGTH_SHORT).show()
        counter = object : CountDownTimer(time, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.timeLeft.text = "$minutes:$seconds"
            }

            override fun onFinish() {
                val editor = preferences.edit()
                editor.putBoolean("timer", false)
                editor.putLong(
                    "endDate",
                    LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
                )
                editor.apply()
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
                editor.apply()
                isTimerActive = false
            } else
                startCounterUI(endDate)
        }
        super.onStart()
    }
}