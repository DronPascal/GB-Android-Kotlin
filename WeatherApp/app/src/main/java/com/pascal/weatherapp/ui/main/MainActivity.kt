package com.pascal.weatherapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.pascal.weatherapp.databinding.ActivityMainBinding
import com.pascal.weatherapp.ui.main.fragments.FragmentsPagerAdapter
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        initPager()
        initTabs()
        initRefresh()
        initFab()
    }

    private fun initPager() {
        binding.viewPager.apply {
            adapter = FragmentsPagerAdapter(this@MainActivity)
            getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun initTabs() {
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(FragmentsPagerAdapter.TAB_TITLES[position])
        }.attach()
    }

    private fun initRefresh() {
        binding.swiperefresh.setOnRefreshListener {
            Thread {
                sleep(1000)
                //mainViewModel.refresh()}
            }.start()
        }
        //mainViewModel.
        binding.swiperefresh.isRefreshing = false;
    }

    private fun initFab() {
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}