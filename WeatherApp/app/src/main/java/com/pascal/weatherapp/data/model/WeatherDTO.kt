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