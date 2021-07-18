package com.analytics.blotout.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.analytics.blotout.data.database.dao.EventDao
import com.analytics.blotout.data.database.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1, exportSchema = true)
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var instance: EventDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                EventDatabase::class.java,
                "events.db"
            ).build()
    }
}