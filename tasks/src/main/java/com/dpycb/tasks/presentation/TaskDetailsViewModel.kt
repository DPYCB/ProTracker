package com.dpycb.tasks.presentation

import androidx.lifecycle.ViewModel
import com.dpycb.tasks.data.Goal
import com.dpycb.tasks.data.Task
import com.dpycb.tasks.domain.ITaskRepository
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class TaskDetailsViewModel @Inject constructor(
    private val tasksRepository: ITaskRepository
): ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    fun getTaskFlow(taskId: Long): Flowable<Task> {
        return tasksRepository.getTasksFlow().map { tasks ->
            tasks.find { it.uid == taskId }
        }
    }

    fun editGoal(taskId: Long, goal: Goal) {
        val task = tasksRepository.getTask(taskId)
        val newGoals = task.goals.toMutableList()
        newGoals.removeIf { it.uid == goal.uid }
        newGoals.add(Goal(goal.uid, goal.name, goal.weight, goal.status))
        tasksRepository.updateTask(task.copy(goals = newGoals))
    }

    fun getGoalById(taskId: Long, goalId: Int): Goal? {
        return tasksRepository.getTask(taskId).goals.find { it.uid == goalId }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}