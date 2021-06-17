package com.pascal.weatherapp.data.remote

import com.pascal.weatherapp.data.model.WeatherDTO
import com.pascal.weatherapp.data.model.WeatherRequest

interface WeatherRepository {

    fun getWeatherDetailsFromServer(
        requestDto: WeatherRequest,
        callback: retrofit2.Callback<WeatherDTO>
    )

}