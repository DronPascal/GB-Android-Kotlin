package com.pascal.weatherapp.ui.home.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pascal.weatherapp.R

/**
 * A [FragmentStateAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class HomeFragmentsPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    companion object {
        val TAB_TITLES = arrayOf(
            R.string.tab_text_today,
            R.string.tab_text_10days
        )
    }

    override fun getItemCount(): Int {
        return TAB_TITLES.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayFragment.newInstance()
            else -> TenDaysFragment.newInstance()
        }
    }
}