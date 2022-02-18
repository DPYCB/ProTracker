package com.dpycb.protracker.data

import com.dpycb.protracker.domain.ITaskRepository
import io.reactivex.Flowable
import javax.inject.Inject

class TasksRepository @Inject constructor(
    private val localDataSource: LocalTasksDataSource
) : ITaskRepository {
    override fun getTasksFlow() = localDataSource.getTasksFlow()
    override fun addTasks(tasks: List<Task>) =  localDataSource.addTasks(tasks)
    override fun updateTask(task: Task) = localDataSource.updateTask(task)
}