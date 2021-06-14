package com.pascal.weatherapp.repository.remote

import com.pascal.weatherapp.model.Weather
import retrofit2.Callback

class WeatherRepositoryImpl(private val remoteWeatherDataSource: RemoteWeatherDataSource) :
    WeatherRepository {
    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: Callback<Weather>
    ) {
        remoteWeatherDataSource.getWeatherDetails(lat, lon, callback)
    }
}