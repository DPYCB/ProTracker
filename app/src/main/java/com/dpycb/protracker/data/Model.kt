package com.dpycb.protracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0L,
    var name: String = "",
    var startDate: Long = 0L,
    var endDate: Long = 0L,
    var progress: Int = 0,
    var goals: List<Goal> = listOf(),
)

@Entity
data class Goal(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    val name: String = "",
    val weight: Int = 1,
    val status: GoalStatus = GoalStatus.NOT_STARTED
)

enum class GoalStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}