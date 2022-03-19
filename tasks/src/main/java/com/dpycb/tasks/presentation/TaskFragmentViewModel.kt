package com.dpycb.tasks.presentation

import androidx.lifecycle.ViewModel
import com.dpycb.tasks.data.Task
import com.dpycb.tasks.domain.ITaskRepository
import io.reactivex.Flowable
import javax.inject.Inject

class TaskFragmentViewModel @Inject constructor(
    private val tasksRepository: ITaskRepository
): ViewModel() {
    fun getTasksFlow(): Flowable<List<TaskViewState>> {
        return tasksRepository
            .getTasksFlow()
            .map { tasks ->
                tasks.map { it.mapToViewState() }
            }
    }

    fun getTotalProgressFlow(): Flowable<String> {
        return tasksRepository
            .getTasksFlow()
            .map { tasks ->
                val totalProgress = tasks.size.toFloat() * 100
                val currentProgress =
                    tasks.filter { it.progress > 0 }.sumOf { it.progress }.toFloat()
                val progressToDisplay =
                    currentProgress / totalProgress.coerceAtLeast(1f) * 100
                "Выполнено ${progressToDisplay.toInt()}%"
            }
    }

    fun removeTask(taskId: Long) {
        tasksRepository.removeTask(taskId)
    }

    private fun Task.mapToViewState(): TaskViewState {
        return TaskViewState(
            this.uid,
            "${this.progress}%",
            this.name,
        )
    }
}