package com.example.androidwithkotlin.model

import com.test.weather.model.entities.Weather

interface Repository {   
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage(): Weather
}
