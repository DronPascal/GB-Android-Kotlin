package com.pascal.weatherapp.repository

import com.pascal.weatherapp.model.Weather

interface MainRepository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalDB(): List<Weather>
}