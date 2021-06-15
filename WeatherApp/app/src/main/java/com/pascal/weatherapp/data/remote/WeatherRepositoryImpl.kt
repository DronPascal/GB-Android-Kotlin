package com.pascal.weatherapp.data.remote

import com.pascal.weatherapp.model.WeatherDTO
import retrofit2.Callback

class WeatherRepositoryImpl(private val remoteWeatherDataSource: RemoteWeatherDataSource) :
    WeatherRepository {
    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    ) {
        remoteWeatherDataSource.getWeatherDetails(lat, lon, callback)
    }
}