package com.dpycb.protracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dpycb.tasks.data.EntityConverters
import com.dpycb.tasks.data.Task
import com.dpycb.tasks.data.TaskDao

@Database(entities = [Task::class], version = 1)
@TypeConverters(EntityConverters::class)
abstract class RoomDb: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}