package com.pascal.weatherapp.data.local

import com.pascal.weatherapp.data.local.room.PositionDao
import com.pascal.weatherapp.data.local.room.PositionEntity
import com.pascal.weatherapp.data.model.Position

class PositionRepositoryImpl(
    private val dataSource: PositionDao
) : PositionRepository {
    override fun findPositionByName(name: String): Position? {
        return convertEntityToPosition(dataSource.getPositionByName(name))
    }

    override fun savePosition(position: Position) {
        dataSource.insert(convertPositionToEntity(position))
    }

    private fun convertEntityToPosition(entity: PositionEntity?): Position? {
        return if (entity == null)
            null
        else
            Position(entity.name, entity.latitude, entity.longitude)
    }


    private fun convertPositionToEntity(position: Position): PositionEntity {
        return with(position) { PositionEntity(0, name, lon, lat) }
    }
}