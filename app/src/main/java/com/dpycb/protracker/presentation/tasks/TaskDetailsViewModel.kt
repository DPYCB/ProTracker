package com.dpycb.protracker.presentation.tasks

import androidx.lifecycle.ViewModel
import com.dpycb.protracker.data.Goal
import com.dpycb.protracker.data.Task
import com.dpycb.protracker.domain.ITaskRepository
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

    fun editGoal(taskId: Long, goal: GoalViewState) {
        val task = tasksRepository.getTask(taskId)
        val newGoals = task.goals.toMutableList()
        newGoals.removeIf { it.uid == goal.id }
        newGoals.add(Goal(goal.id, goal.name, goal.weight, goal.status))
        tasksRepository.updateTask(task.copy(goals = newGoals))
    }

    fun getGoalById(taskId: Long,goalId: Int): GoalViewState? {
        return tasksRepository.getTask(taskId).goals.find { it.uid == goalId }?.toGoalViewState()
    }

    fun getGoalsViewState(goals: List<Goal>): List<GoalViewState> {
        return goals.toGoalViewStates()
    }

    private fun List<Goal>.toGoalViewStates(): List<GoalViewState> {
        return this.map { it.toGoalViewState() }
    }

    private fun Goal.toGoalViewState(): GoalViewState {
        return GoalViewState(
            this.uid,
            this.name,
            this.weight,
            this.status
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}