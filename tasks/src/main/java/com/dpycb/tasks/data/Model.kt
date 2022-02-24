package com.dpycb.tasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

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

data class Goal(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    val name: String = "",
    val weight: Int = 1,
    val status: GoalStatus = GoalStatus.NOT_STARTED
)

enum class GoalStatus(val statusName: String) {
    NOT_STARTED("Не начато"),
    IN_PROGRESS("В процессе"),
    COMPLETED("Завершено")
}

class EntityConverters {
    @TypeConverter
    fun goalsToGson(goals: List<Goal>) = Gson().toJson(goals)

    @TypeConverter
    fun gsonToGoals(value: String) = Gson().fromJson(value, Array<Goal>::class.java).toList()
}