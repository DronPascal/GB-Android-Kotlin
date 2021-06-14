package com.pascal.weatherapp.repository.remote

import com.pascal.weatherapp.model.Weather

interface WeatherRepository {
    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<Weather>
    )
}