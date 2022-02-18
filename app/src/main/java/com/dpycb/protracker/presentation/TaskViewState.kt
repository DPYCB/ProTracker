package com.dpycb.protracker.presentation

data class TaskViewState(
    val taskId: Long,
    val progress: String,
    val title: String,
    val endDate: String
)