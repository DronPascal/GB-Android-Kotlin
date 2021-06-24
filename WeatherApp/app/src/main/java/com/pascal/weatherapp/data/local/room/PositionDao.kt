package com.pascal.weatherapp.data.local.room

import androidx.room.*

@Dao
interface PositionDao {

    @Query("SELECT * FROM PositionEntity")
    fun all(): List<PositionEntity>

    @Query("SELECT * FROM PositionEntity WHERE name LIKE :name")
    fun getDataByWord(name: String): List<PositionEntity>

    @Query("SELECT * FROM PositionEntity WHERE name LIKE :name LIMIT 1")
    fun getPositionByName(name: String): PositionEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: PositionEntity)

    @Update
    fun update(entity: PositionEntity)

    @Delete
    fun delete(entity: PositionEntity)
}