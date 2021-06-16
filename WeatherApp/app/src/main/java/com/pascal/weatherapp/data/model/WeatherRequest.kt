package com.pascal.weatherapp.data.model

class WeatherRequest(
    val lat: Double,
    val lon: Double,
    val lang: String = "ru_RU"
)