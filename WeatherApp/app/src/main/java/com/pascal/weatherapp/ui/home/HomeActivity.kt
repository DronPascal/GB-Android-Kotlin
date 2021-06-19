package com.pascal.weatherapp.ui.home

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.pascal.weatherapp.R
import com.pascal.weatherapp.databinding.HomeActivityBinding
import com.pascal.weatherapp.ui.MainViewModel
import com.pascal.weatherapp.ui.home.fragments.FragmentsPagerAdapter


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeActivityBinding
    private lateinit var receiver: BroadcastReceiver
    private lateinit var snackbar: Snackbar
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setContentView(binding.root)

        initView()

        mainViewModel.initiateWeatherRefresh()
    }

    private fun initView() {
        initPager()
        initTabs()
        initFab()
        initSnackbar()
        initReceiver()
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
            Snackbar.make(view, "No action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun initReceiver() {
        receiver = object : ConnectBroadcastReceiver() {
            override fun onNetworkChange(intent: Intent) {
                when (intent.extras?.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY)) {
                    false -> snackbar.dismiss()
                    true -> snackbar.show()
                }
            }
        }
        // TODO CONNECTIVITY_ACTION Deprecated
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(receiver, filter)
    }

    private fun initSnackbar() {
        snackbar = Snackbar.make(
            binding.root,
            getString(R.string.snackbar_check_connection_msg),
            Snackbar.LENGTH_INDEFINITE
        )
        val params = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackbar.view.layoutParams = params
        snackbar.setAction(getString(R.string.close)) { snackbar.dismiss() }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
