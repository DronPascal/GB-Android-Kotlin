package com.pascal.weatherapp.app

import android.app.Application
import androidx.room.Room
import com.pascal.weatherapp.data.local.room.PositionDao
import com.pascal.weatherapp.data.local.room.PositionDatabase

class App : Application() {

    init {
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private var db: PositionDatabase? = null
        private const val DB_NAME = "Positions.db"

        fun getPositionDao(): PositionDao {
            if (db == null) {
                synchronized(PositionDatabase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            PositionDatabase::class.java,
                            DB_NAME
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }

            return db!!.positionDao()
        }
    }
}
