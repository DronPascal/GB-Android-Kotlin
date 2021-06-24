package com.pascal.weatherapp.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PositionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val name: String,
    val latitude: Double,
    val longitude: Double

)