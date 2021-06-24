package com.pascal.weatherapp.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


abstract class ConnectBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        onNetworkChange(intent)
    }

    protected abstract fun onNetworkChange(intent: Intent)
}