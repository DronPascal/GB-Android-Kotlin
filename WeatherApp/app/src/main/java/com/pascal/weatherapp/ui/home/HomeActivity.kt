package com.pascal.weatherapp.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.pascal.weatherapp.data.model.City
import com.pascal.weatherapp.data.model.WeatherRequest
import com.pascal.weatherapp.databinding.HomeActivityBinding
import com.pascal.weatherapp.ui.MainViewModel
import com.pascal.weatherapp.ui.home.fragments.FragmentsPagerAdapter

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeActivityBinding

    val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        City.getDefaultCity().let {
            mainViewModel.getWeatherFromRemoteSource(WeatherRequest(it.lat, it.lon))
        }
    }

    private fun initView() {
        initPager()
        initTabs()
        initFab()
    }

    private fun initPager() {
        binding.viewPager.apply {
            adapter = FragmentsPagerAdapter(this@HomeActivity)
            getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun initTabs() {
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(FragmentsPagerAdapter.TAB_TITLES[position])
        }.attach()
    }

    private fun initFab() {
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}
