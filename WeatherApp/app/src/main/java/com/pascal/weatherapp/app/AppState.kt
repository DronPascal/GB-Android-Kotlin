package com.pascal.weatherapp.app

sealed class AppState {
    object Success : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
