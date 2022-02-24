package com.dpycb.tasks.data

import com.dpycb.tasks.domain.ITaskRepository
import com.dpycb.utils.di.ActivityScope
import com.dpycb.utils.runOnIo
import javax.inject.Inject

@ActivityScope
class TasksRepository @Inject constructor(
    private val localDataSource: LocalTasksDataSource
) : ITaskRepository {
    override fun getTasksFlow() = localDataSource.getTasksFlow()
    override fun getTask(taskId: Long) = localDataSource.getTask(taskId)
    override fun addTasks(tasks: List<Task>) {
        runOnIo {
            localDataSource.addTasks(tasks)
        }
    }

    override fun updateTask(task: Task) {
        runOnIo {
            localDataSource.updateTask(task)
        }
    }

    override fun removeAllTasks() {
        runOnIo {
            localDataSource.removeAllTasks()
        }
    }

    override fun removeTask(taskId: Long) {
        runOnIo {
            localDataSource.removeTask(taskId)
        }
    }
}