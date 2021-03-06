package com.dpycb.tasks.presentation

import androidx.lifecycle.ViewModel
import com.dpycb.tasks.data.Goal
import com.dpycb.tasks.data.GoalStatus
import com.dpycb.tasks.data.Task
import com.dpycb.tasks.domain.ITaskRepository
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import java.util.*
import javax.inject.Inject
import kotlin.reflect.jvm.internal.impl.utils.CollectionsKt

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
        val newGoal = Goal(
            uid = currentList.size,
            name = name,
            weight = weight,
            status = status
        )
        val resultList = listOf(newGoal).plus(currentList).sortedBy { it.uid }
        goalsProcessor.onNext(resultList)
    }

    fun editGoal(editedGoal: Goal) {
        val currentList = goalsProcessor.value ?: return
        val filteredList = currentList.filter { it.uid != editedGoal.uid }.toMutableList()
        filteredList.add(editedGoal)
        goalsProcessor.onNext(filteredList.sortedBy { it.uid })
    }

    fun getGoalsFlow(): Flowable<List<Goal>> {
        return goalsProcessor
    }

    fun getGoalById(goalId: Int): Goal? {
        val currentGoals = goalsProcessor.value
        return currentGoals?.find { it.uid == goalId }
    }

    private fun getGoalsForTask(): List<Goal> {
        return goalsProcessor.value
            ?.filter { it.weight in (1..10) } ?: listOf()
    }


    private fun getFooterGoal(): Goal {
        return Goal(
            uid = AddTaskBSFragment.NEW_GOAL_ID,
            name = "+ ?????????? ????????",
            weight = 0,
        )
    }

    override fun onCleared() {
        super.onCleared()
        goalsProcessor.onComplete()
    }
}