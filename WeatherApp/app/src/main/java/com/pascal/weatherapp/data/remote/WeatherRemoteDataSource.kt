package com.pascal.weatherapp.data.remote

import com.google.gson.GsonBuilder
import com.pascal.weatherapp.BuildConfig
import com.pascal.weatherapp.data.model.WeatherDTO
import com.pascal.weatherapp.data.model.WeatherRequest
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class WeatherRemoteDataSource {

    private val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.weather.yandex.ru/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(createOkHttpClient(WeatherApiInterceptor()))
        .build().create(WeatherAPI::class.java)

    fun getWeatherDetails(requestDto: WeatherRequest, callback: Callback<WeatherDTO>) {
        with(requestDto) {
            weatherApi.getWeather(BuildConfig.WEATHER_API_KEY, lat, lon, lang).enqueue(callback)
        }
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }

    inner class WeatherApiInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }

}