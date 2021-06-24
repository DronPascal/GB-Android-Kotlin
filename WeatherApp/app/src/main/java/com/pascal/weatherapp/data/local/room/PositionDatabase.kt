package com.pascal.weatherapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PositionEntity::class], version = 1, exportSchema = false)
abstract class PositionDatabase : RoomDatabase() {

    abstract fun positionDao(): PositionDao
}
