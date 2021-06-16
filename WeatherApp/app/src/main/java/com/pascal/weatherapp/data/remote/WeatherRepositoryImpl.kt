package com.pascal.weatherapp.data.remote

import com.pascal.weatherapp.data.model.WeatherDTO
import com.pascal.weatherapp.data.model.WeatherRequest
import retrofit2.Callback

class WeatherRepositoryImpl(private val weatherRemoteDataSource: WeatherRemoteDataSource) :
    WeatherRepository {
    override fun getWeatherDetailsFromServer(
        requestDto: WeatherRequest,
        callback: Callback<WeatherDTO>
    ) {
        weatherRemoteDataSource.getWeatherDetails(requestDto, callback)
    }
}