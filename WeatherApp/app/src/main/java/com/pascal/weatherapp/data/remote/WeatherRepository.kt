package com.pascal.weatherapp.data.remote

import com.pascal.weatherapp.model.WeatherDTO

interface WeatherRepository {

    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    )
}