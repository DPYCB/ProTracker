package com.dpycb.tasks.data

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAllTasks(): Flowable<List<Task>>

    @Query("SELECT * FROM task WHERE uid = :arg0 LIMIT 1")
    fun getTaskById(uid: Long): Task

    @Query("DELETE FROM task")
    fun clearTable()

    @Query("DELETE FROM task WHERE uid = :arg0")
    fun removeTask(taskId: Long)

    @Insert
    fun addTasks(tasks: List<Task>)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)
}