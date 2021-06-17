package com.pascal.weatherapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherDTO(
    val now: Int?,
    val now_dt: String?,
    val info: InfoDTO?,
    val fact: FactDTO?,
    val forecast: ForecastDTO?
) : Parcelable

@Parcelize
data class InfoDTO(
    val lat: Double?,
    val lon: Double?,
    val url: String?
) : Parcelable

@Parcelize
data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val icon: String?,
    val condition: String?
) : Parcelable

@Parcelize
data class ForecastDTO(
    val parts: List<PartsDTO>?
) : Parcelable

@Parcelize
data class PartsDTO(
    val temp_min: Int?,
    val temp_max: Int?,
    val prec_prob: Int?
) : Parcelable

val testWeatherDTO
    get() = WeatherDTO(
        now = 1623938412,
        now_dt = "2021-06-17T14:00:12.130872Z",
        info = InfoDTO(
            lat = 55.755826,
            lon = 37.617299900000035,
            url = "https://yandex.ru/pogoda/213?lat=55.755826\u0026lon=37.6172999"
        ),
        fact = FactDTO(
            temp = (-20..30).random(),
            feels_like = (-20..30).random(),
            icon = "skc_d",
            condition = "clear"
        ),
        forecast = ForecastDTO(
            parts = listOf(
                PartsDTO(
                    temp_min = (-20..30).random(),
                    temp_max = (-20..30).random(),
                    prec_prob = (0..99).random()
                )
            )
        )
    )