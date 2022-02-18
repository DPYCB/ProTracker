package com.dpycb.protracker.domain

import com.dpycb.protracker.data.Task
import io.reactivex.Flowable

interface ITaskRepository {
    fun getTasksFlow(): Flowable<List<Task>>
    fun addTasks(tasks: List<Task>)
    fun updateTask(task: Task)
    fun removeAllTasks(): Any
}