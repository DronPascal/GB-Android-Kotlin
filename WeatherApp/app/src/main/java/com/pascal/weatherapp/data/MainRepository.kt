package com.pascal.weatherapp.data

import com.pascal.weatherapp.data.model.WeatherDTO

interface MainRepository {
    fun getWeatherFromServer(): WeatherDTO
    fun getWeatherFromLocalDB(): List<WeatherDTO>
}