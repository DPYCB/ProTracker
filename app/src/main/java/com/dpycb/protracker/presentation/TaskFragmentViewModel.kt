package com.dpycb.protracker.presentation

import androidx.lifecycle.ViewModel
import com.dpycb.protracker.data.Task
import com.dpycb.protracker.domain.ITaskRepository
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
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
                if (tasks.isNotEmpty()) {
                    var totalProgress = 0
                    tasks.forEach { totalProgress += it.progress }
                    "Выполнено ${totalProgress/tasks.size}%"
                }
                else {
                    "Выполнено 0"
                }
            }
    }

    fun addRandomTask() {
        tasksRepository.addTasks(listOf(
            Task(
                uid = (0..10000L).random(),
                progress = (0..100).random(),
                name = listOf(
                    "Попробовать поесть",
                    "Сходить покакать",
                    "Покурить кальян",
                    "Полежать",
                    "Кайфануть",
                    "ФлЭксить"
                ).random()
            )
        ))
    }

    fun removeAllTasks() {
        tasksRepository.removeAllTasks()
    }

    private fun Task.mapToViewState(): TaskViewState {
        return TaskViewState(
            this.uid,
            "${this.progress}%",
            this.name,
            this.endDate.toString()
        )
    }
}