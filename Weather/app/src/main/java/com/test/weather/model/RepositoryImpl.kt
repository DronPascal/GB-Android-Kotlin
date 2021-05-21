package com.example.androidwithkotlin.model

import com.test.weather.model.entities.Weather

class RepositoryImpl : Repository {

    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorage(): Weather {
        return Weather()
    }
}
