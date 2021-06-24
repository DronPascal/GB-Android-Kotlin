package com.pascal.weatherapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Position(
    val name: String,
    val lat: Double,
    val lon: Double
) : Parcelable {
    companion object {
        fun getDefaultPosition() = Position("Москва", 55.755826, 37.617299900000035)
    }
}
