package com.freshly.interview.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EventDbo::class, UpdateTimeDbo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    abstract fun updateTimeDao(): UpdateTimeDao

}