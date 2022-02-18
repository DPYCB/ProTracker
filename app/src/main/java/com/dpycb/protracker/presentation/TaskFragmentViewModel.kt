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
    fun getRandomTask(): Task? {
        return tasksRepository
            .getTasksFlow()
            .observeOn(Schedulers.newThread())
            .blockingFirst()
            .randomOrNull()
    }

    fun addRandomTask() {
        tasksRepository.addTasks(listOf(
            Task(
                uid = (0..10000L).random()
            )
        ))
    }
}