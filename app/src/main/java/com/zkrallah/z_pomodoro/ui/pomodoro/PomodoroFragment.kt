package com.zkrallah.z_pomodoro.ui.pomodoro

import android.annotation.SuppressLint
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

class PomodoroFragment : Fragment() {
    private lateinit var binding: FragmentPomodoroBinding
    private lateinit var counter: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPomodoroBinding.inflate(inflater, container, false)

        val scheduler = AndroidAlarmScheduler(requireContext())
        var alarmItem: Alarm? = null

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

}