package com.dpycb.protracker.data

import com.dpycb.protracker.di.ActivityScope
import com.dpycb.protracker.domain.ITaskRepository
import javax.inject.Inject

@ActivityScope
class TasksRepository @Inject constructor(
    private val localDataSource: LocalTasksDataSource
) : ITaskRepository {
    override fun getTasksFlow() = localDataSource.getTasksFlow()
    override fun getTask(taskId: Long) = localDataSource.getTask(taskId)
    override fun addTasks(tasks: List<Task>) =  localDataSource.addTasks(tasks)
    override fun updateTask(task: Task) = localDataSource.updateTask(task)
    override fun removeAllTasks() = localDataSource.removeAllTasks()
    override fun removeTask(taskId: Long) = localDataSource.removeTask(taskId)
}