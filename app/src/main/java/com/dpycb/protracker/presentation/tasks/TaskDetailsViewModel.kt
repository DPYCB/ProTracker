package com.dpycb.protracker.presentation.tasks

import androidx.lifecycle.ViewModel
import com.dpycb.protracker.data.Task
import com.dpycb.protracker.domain.ITaskRepository
import io.reactivex.Flowable
import javax.inject.Inject

class TaskDetailsViewModel @Inject constructor(
    private val tasksRepository: ITaskRepository
): ViewModel() {
    fun getTaskFlow(taskId: Long): Flowable<Task> {
        return tasksRepository.getTasksFlow().map { tasks ->
            tasks.find { it.uid == taskId }
        }
    }
}