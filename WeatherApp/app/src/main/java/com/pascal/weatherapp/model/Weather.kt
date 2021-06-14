package com.pascal.weatherapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
data class Weather(
    val city: City = getDefaultCity(),
    val url: String = "https://yandex.ru/pogoda/moscow",
    val forecastTimestamp: Timestamp = Timestamp(System.currentTimeMillis() / 1000),

    val curTemp: Int = 0,
    val feelsLike: Int = 0,
    val condition: String = "Солнечно",
    val icon: String = "bkn_n",
    val maxTemp: Int = 0,
    val minTemp: Int = 0,
    val precProbability: Int = 0
) : Parcelable {
    companion object {
        fun getDefaultCity() = City("Москва", 55.755826, 37.617299900000035)
    }
}

