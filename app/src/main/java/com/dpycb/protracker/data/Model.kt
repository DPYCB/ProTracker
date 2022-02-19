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
)