package com.dpycb.protracker.di.tasks

import com.dpycb.protracker.data.RoomDb
import com.dpycb.tasks.data.TaskDao
import com.dpycb.tasks.data.TasksRepository
import com.dpycb.tasks.domain.ITaskRepository
import com.dpycb.utils.di.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class TasksProvidesModule {
    @ActivityScope
    @Provides
    fun provideRepository(repo: TasksRepository): ITaskRepository {
        return repo
    }

    @Provides
    fun provideTaskDao(database: RoomDb): TaskDao {
        return database.taskDao()
    }
}