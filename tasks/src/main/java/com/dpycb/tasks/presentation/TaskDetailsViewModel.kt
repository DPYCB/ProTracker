package com.dpycb.tasks.presentation

import androidx.lifecycle.ViewModel
import com.dpycb.tasks.data.Goal
import com.dpycb.tasks.data.GoalStatus
import com.dpycb.tasks.data.Task
import com.dpycb.tasks.domain.ITaskRepository
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaskDetailsViewModel @Inject constructor(
    private val tasksRepository: ITaskRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    fun getTaskFlow(taskId: Long): Flowable<Task> {
        return tasksRepository.getTasksFlow().map { tasks ->
            tasks.find { it.uid == taskId }
        }
    }

    fun editGoal(taskId: Long, goal: Goal) {
        Maybe.just(true)
            .observeOn(Schedulers.io())
            .subscribe {
                val task = tasksRepository.getTask(taskId)
                val newGoals = task.goals.toMutableList()
                newGoals.removeIf { it.uid == goal.uid }
                newGoals.add(Goal(goal.uid, goal.name, goal.weight, goal.status))

                tasksRepository.updateTask(
                    task.copy(
                        progress = getCurrentTaskProgress(newGoals),
                        goals = newGoals.sortedByDescending { it.uid }
                    )
                )
            }.let(compositeDisposable::add)
    }

    private fun getCurrentTaskProgress(updatedGoals: List<Goal>): Int {
        val totalGoalsWeight = updatedGoals.sumOf { it.weight }.toFloat()
        val completedGoalsWeight = updatedGoals
            .filter { it.status == GoalStatus.COMPLETED }
            .sumOf { it.weight }
            .toFloat()
        return (completedGoalsWeight / totalGoalsWeight * 100).toInt()
    }

    fun getGoalById(taskId: Long, goalId: Int): Goal? {
        return tasksRepository.getTask(taskId).goals.find { it.uid == goalId }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}