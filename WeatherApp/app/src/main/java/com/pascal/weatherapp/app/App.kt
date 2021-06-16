package com.pascal.weatherapp.app

import android.app.Application

class App : Application() {

    companion object {
        private var appInstance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
}
