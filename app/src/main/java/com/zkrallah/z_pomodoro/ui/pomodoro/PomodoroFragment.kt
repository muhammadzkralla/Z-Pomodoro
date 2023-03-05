package com.zkrallah.z_pomodoro.ui.pomodoro

import android.os.Bundle
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
        }
        binding.cancelBtn.setOnClickListener {
            scheduler.cancel(alarmItem!!)
        }
        return binding.root
    }

}