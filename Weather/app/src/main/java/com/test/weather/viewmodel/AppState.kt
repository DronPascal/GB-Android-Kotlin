package com.example.androidwithkotlin.viewmodel

import com.example.androidwithkotlin.model.Weather

sealed class AppState {
    data class Success(val weatherData: Weather) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
