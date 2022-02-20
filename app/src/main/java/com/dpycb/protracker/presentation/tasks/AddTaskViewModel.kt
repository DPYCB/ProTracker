package com.dpycb.protracker.presentation.tasks

import androidx.lifecycle.ViewModel
import com.dpycb.protracker.data.Goal
import com.dpycb.protracker.data.GoalStatus
import com.dpycb.protracker.data.Task
import com.dpycb.protracker.domain.ITaskRepository
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

class AddTaskViewModel @Inject constructor(
    private val tasksRepository: ITaskRepository
) : ViewModel() {
    private val goalsProcessor = BehaviorProcessor.createDefault(listOf(getFooterGoal()))

    fun addNewTask(
        name: String,
        startDate: Long,
        endDate: Long,
    ) {
        val task = Task(
            name = name,
            startDate = startDate,
            endDate = endDate,
            goals = getGoalsForTask(),
        )
        tasksRepository.addTasks(listOf(task))
    }

    fun addNewGoal(name: String, weight: Int, status: GoalStatus) {
        val currentList = goalsProcessor.value ?: listOf(getFooterGoal())
        val newGoal = GoalViewState(
            id = currentList.size,
            name = name,
            weight = weight,
            status = status
        )
        val resultList = listOf(newGoal).plus(currentList).sortedByDescending { it.id }
        goalsProcessor.onNext(resultList)
    }

    fun editGoal(editedGoal: GoalViewState) {
        val currentList = goalsProcessor.value?.toMutableList() ?: return
        currentList.removeIf { it.id == editedGoal.id }
        currentList.add(editedGoal)
        goalsProcessor.onNext(currentList.sortedByDescending { it.id })
    }

    fun getGoalsFlow(): Flowable<List<GoalViewState>> {
        return goalsProcessor
    }

    fun getGoalById(goalId: Int): GoalViewState? {
        val currentGoals = goalsProcessor.value
        return currentGoals?.find { it.id == goalId }
    }

    private fun getGoalsForTask(): List<Goal> {
        return goalsProcessor.value
            ?.filter { it.weight in (1..10) }
            ?.map {
                Goal(
                    name = it.name,
                    weight = it.weight
                )
            } ?: listOf()
    }


    private fun getFooterGoal(): GoalViewState {
        return GoalViewState(
            id = AddTaskFragment.NEW_GOAL_ID,
            name = "+ Новая цель",
            weight = 0,
        )
    }

    override fun onCleared() {
        super.onCleared()
        goalsProcessor.onComplete()
    }
}