@file:Suppress("DEPRECATION")

package com.zkrallah.z_pomodoro.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zkrallah.z_pomodoro.R
import com.zkrallah.z_pomodoro.ui.alarm.AlarmFragment
import com.zkrallah.z_pomodoro.ui.pomodoro.PomodoroFragment
import com.zkrallah.z_pomodoro.ui.stats.StatsFragment


private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AlarmFragment()
            1 -> PomodoroFragment()
            2 -> StatsFragment()
            else -> AlarmFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }
}