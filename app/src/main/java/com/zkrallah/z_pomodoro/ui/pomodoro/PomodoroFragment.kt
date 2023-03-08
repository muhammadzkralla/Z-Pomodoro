package com.zkrallah.z_pomodoro.ui.pomodoro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zkrallah.z_pomodoro.alarm.PomodoroScheduler
import com.zkrallah.z_pomodoro.databinding.FragmentPomodoroBinding
import java.time.LocalDateTime

class PomodoroFragment : Fragment() {
    private lateinit var binding: FragmentPomodoroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPomodoroBinding.inflate(inflater, container, false)

        binding.startBtn.setOnClickListener {
            val duration = binding.edtTimer.text.toString().toLong()
            val serviceIntent = Intent(activity, PomodoroScheduler::class.java).apply {
                putExtra("time",
                    (LocalDateTime.now().plusSeconds(duration)).toString())
            }
            activity?.startForegroundService(serviceIntent)
        }
        binding.cancelBtn.setOnClickListener {
            val serviceIntent = Intent(requireActivity(), PomodoroScheduler::class.java)
            activity?.stopService(serviceIntent)
            binding.timeLeft.text = "0:0"
        }
        return binding.root
    }

//    override fun onStop() {
//        preferences = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE)
//        val editor = preferences.edit()
//        if (alarmItem != null){
//            editor.putBoolean("timerActive", true)
//            editor.putLong("endTime", alarmItem!!.time.atZone(ZoneId.systemDefault()).toEpochSecond())
//            editor.apply()
//        }
//        super.onStop()
//    }
//
//    override fun onStart() {
//        preferences = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE)
//        val timerActive = preferences.getBoolean("timerActive", false)
//        if (timerActive){
//            val endTime = preferences.getLong("endTime", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond())
//            startCountDown(endTime.minus(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()) * 1000)
//        }
//        super.onStart()
//    }

}