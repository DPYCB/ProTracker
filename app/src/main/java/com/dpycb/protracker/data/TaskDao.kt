package com.dpycb.protracker.data

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAllTasks(): Flowable<List<Task>>

    @Query("SELECT * FROM task WHERE uid = :uid LIMIT 1")
    fun getTaskById(uid: Long): Task

    @Query("DELETE FROM task")
    fun clearTable()

    @Insert
    fun addTasks(tasks: List<Task>)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)
}