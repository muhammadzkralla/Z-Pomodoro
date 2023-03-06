package com.zkrallah.z_pomodoro.ui.pomodoro

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zkrallah.z_pomodoro.alarm.AndroidAlarmScheduler
import com.zkrallah.z_pomodoro.databinding.FragmentPomodoroBinding
import com.zkrallah.z_pomodoro.model.Alarm
import java.time.LocalDateTime
import java.time.ZoneId

class PomodoroFragment : Fragment() {
    private lateinit var binding: FragmentPomodoroBinding
    private lateinit var counter: CountDownTimer
    private lateinit var preferences: SharedPreferences
    var alarmItem: Alarm? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPomodoroBinding.inflate(inflater, container, false)

        val scheduler = AndroidAlarmScheduler(requireContext())

        binding.startBtn.setOnClickListener {
            val duration = binding.edtTimer.text.toString().toLong()
            alarmItem = Alarm(LocalDateTime.now().plusSeconds(duration), duration.toString())
            scheduler.schedule(alarmItem!!)
            startCountDown(scheduler.timeLeft)
        }
        binding.cancelBtn.setOnClickListener {
            scheduler.cancel(alarmItem!!)
            counter.cancel()
            binding.timeLeft.text = "0:0"
        }
        return binding.root
    }

    private fun startCountDown(timeLeft: Long){
        counter = object: CountDownTimer(timeLeft, 1000){
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val minute = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.timeLeft.text = "$minute:$seconds"
            }

            override fun onFinish() {

            }

        }
        counter.start()
    }

    override fun onStop() {
        preferences = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = preferences.edit()
        if (alarmItem != null){
            editor.putBoolean("timerActive", true)
            editor.putLong("endTime", alarmItem!!.time.atZone(ZoneId.systemDefault()).toEpochSecond())
            editor.apply()
        }
        super.onStop()
    }

    override fun onStart() {
        preferences = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE)
        val timerActive = preferences.getBoolean("timerActive", false)
        if (timerActive){
            val endTime = preferences.getLong("endTime", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond())
            startCountDown(endTime.minus(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()) * 1000)
        }
        super.onStart()
    }

}