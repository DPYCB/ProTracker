package com.dpycb.tasks.presentation

import androidx.lifecycle.ViewModel
import com.dpycb.tasks.data.Task
import com.dpycb.tasks.domain.ITaskRepository
import com.dpycb.utils.Utils
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
                var totalProgress = 0
                tasks.forEach { totalProgress += it.progress }
                "Выполнено ${totalProgress/tasks.size.coerceAtLeast(1)}%"
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
            Utils.formatDateToString(this.endDate)
        )
    }
}