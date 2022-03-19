package com.dpycb.tasks.presentation

data class TaskViewState(
    val taskId: Long,
    val progress: String,
    val title: String,
)