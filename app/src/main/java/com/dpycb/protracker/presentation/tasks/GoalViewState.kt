package com.dpycb.protracker.presentation.tasks

import com.dpycb.protracker.data.GoalStatus

data class GoalViewState(
    val id: Int = 0,
    val name: String = "",
    val weight: Int = 1,
    val status: GoalStatus = GoalStatus.NOT_STARTED,
)