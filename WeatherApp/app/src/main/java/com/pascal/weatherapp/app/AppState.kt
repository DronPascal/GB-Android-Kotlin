package com.pascal.weatherapp.app

import com.pascal.weatherapp.model.WeatherDTO

sealed class AppState {
    data class Success(val weatherData: List<WeatherDTO>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
