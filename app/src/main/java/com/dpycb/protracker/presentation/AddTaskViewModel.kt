package com.dpycb.protracker.presentation

import androidx.lifecycle.ViewModel
import com.dpycb.protracker.data.Task
import com.dpycb.protracker.data.TasksRepository
import com.dpycb.protracker.domain.ITaskRepository
import javax.inject.Inject

class AddTaskViewModel @Inject constructor(
    private val tasksRepository: ITaskRepository
): ViewModel() {
    fun addNewTask(name: String, startDate: Long, endDate: Long) {
        val task = Task(
            name = name,
            startDate = startDate,
            endDate = endDate
        )
        tasksRepository.addTasks(listOf(task))
    }
}