package com.dpycb.tasks.data

import javax.inject.Inject

class LocalTasksDataSource @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getTasksFlow() = taskDao.getAllTasks()
    fun getTask(taskId: Long) = taskDao.getTaskById(taskId)
    fun addTasks(tasks: List<Task>) = taskDao.addTasks(tasks)
    fun updateTask(task: Task) = taskDao.updateTask(task)
    fun removeAllTasks() = taskDao.clearTable()
    fun removeTask(taskId: Long) = taskDao.removeTask(taskId)
}