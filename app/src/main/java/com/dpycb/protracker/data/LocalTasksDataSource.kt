package com.dpycb.protracker.data

import javax.inject.Inject

class LocalTasksDataSource @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getTasksFlow() = taskDao.getAllTasks()
    fun addTasks(tasks: List<Task>) = taskDao.addTasks(tasks)
    fun updateTask(task: Task) = taskDao.updateTask(task)
    fun removeAllTasks() = taskDao.clearTable()
}