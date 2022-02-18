package com.dpycb.protracker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class RoomDb: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}