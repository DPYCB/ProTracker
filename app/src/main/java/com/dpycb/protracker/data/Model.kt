package com.dpycb.protracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey
    val uid: Long,
    val name: String = "",
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val progress: Int = 0,
)