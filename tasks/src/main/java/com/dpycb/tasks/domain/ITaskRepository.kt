package com.dpycb.tasks.domain

import com.dpycb.tasks.data.Task
import io.reactivex.Flowable

interface ITaskRepository {
    fun getTasksFlow(): Flowable<List<Task>>
    fun addTasks(tasks: List<Task>)
    fun updateTask(task: Task)
    fun removeAllTasks(): Any
    fun removeTask(taskId: Long)
    fun getTask(taskId: Long): Task
}