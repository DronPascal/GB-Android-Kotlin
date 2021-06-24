package com.pascal.weatherapp.data.local

import com.pascal.weatherapp.data.model.Position

interface PositionRepository {
    fun findPositionByName(name: String): Position?
    fun savePosition(position: Position)
}