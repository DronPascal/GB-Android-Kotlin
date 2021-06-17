package com.pascal.weatherapp.utils

import androidx.annotation.StringRes
import com.pascal.weatherapp.R

@StringRes
fun resFromCondition(conditionKey: String): Int {
    return when (conditionKey) {
        "clear" -> R.string.condition_clear
        "partly-cloudy" -> R.string.condition_partly_cloudy
        "cloudy" -> R.string.condition_cloudy
        "overcast" -> R.string.condition_overcast
        "drizzle" -> R.string.condition_drizzle
        "light-rain" -> R.string.condition_light_rain
        "rain" -> R.string.condition_rain
        "moderate_rain" -> R.string.condition_moderate_rain
        "heavy-rain" -> R.string.condition_heavy_rain
        "continuous-heavy-rain" -> R.string.condition_continuous_heavy_rain
        "showers" -> R.string.condition_showers
        "wet-snow" -> R.string.condition_wet_snow
        "light-snow" -> R.string.condition_light_snow
        "snow" -> R.string.condition_snow
        "snow-showers" -> R.string.condition_snow_showers
        "hail" -> R.string.condition_hail
        "thunderstorm" -> R.string.condition_thunderstorm
        "thunderstorm_with-rain" -> R.string.condition_thunderstorm_with_rain
        "thunderstorm_with-hail" -> R.string.condition_thunderstorm_with_hail
        else -> R.string.condition_blank
    }
}