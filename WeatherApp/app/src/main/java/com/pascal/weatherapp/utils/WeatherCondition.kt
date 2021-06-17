package com.pascal.weatherapp.utils

import androidx.annotation.StringRes
import com.pascal.weatherapp.R

@StringRes
fun resFromCondition(conditionKey: String): Int {
    return when (conditionKey) {
        "clear" -> R.string.condition_clear
        "partly_cloudy" -> R.string.condition_partly_cloudy
        "cloudy" -> R.string.condition_cloudy
        "overcast" -> R.string.condition_overcast
        "drizzle" -> R.string.condition_drizzle
        "light_rain" -> R.string.condition_light_rain
        "rain" -> R.string.condition_rain
        "moderate_rain" -> R.string.condition_moderate_rain
        "heavy_rain" -> R.string.condition_heavy_rain
        "continuous_heavy_rain" -> R.string.condition_continuous_heavy_rain
        "showers" -> R.string.condition_showers
        "wet_snow" -> R.string.condition_wet_snow
        "light_snow" -> R.string.condition_light_snow
        "snow" -> R.string.condition_snow
        "snow_showers" -> R.string.condition_snow_showers
        "hail" -> R.string.condition_hail
        "thunderstorm" -> R.string.condition_thunderstorm
        "thunderstorm_with_rain" -> R.string.condition_thunderstorm_with_rain
        "thunderstorm_with_hail" -> R.string.condition_thunderstorm_with_hail
        else -> R.string.condition_blank
    }
}