package com.dpycb.protracker.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import com.dpycb.protracker.data.RoomDb
import com.dpycb.protracker.data.TaskDao
import com.dpycb.protracker.data.TasksRepository
import com.dpycb.protracker.domain.ITaskRepository
import com.dpycb.protracker.presentation.MainActivity
import com.dpycb.protracker.presentation.TaskFragment
import com.dpycb.protracker.presentation.TaskFragmentViewModel
import dagger.*
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [
        AndroidSupportInjectionModule::class,
        TaskFragmentModule::class,
        MainActivityBindsModule::class,
        TasksModule::class
    ]
)
interface MainActivityComponent {
    val viewModelFactory: DaggerViewModelFactory
    fun inject(activity: MainActivity)
}

@Module
abstract class TaskFragmentModule {
    @ContributesAndroidInjector
    abstract fun getFragment(): TaskFragment
}

@Module
interface MainActivityBindsModule {
    @Binds
    @[IntoMap ViewModelKey(TaskFragmentViewModel::class)]
    fun getTaskViewModel(viewModel: TaskFragmentViewModel): ViewModel
}

@Module
class TasksModule {
    @Provides
    fun provideRepository(repo: TasksRepository): ITaskRepository {
        return repo
    }

    @Provides
    fun provideTaskDao(database: RoomDb): TaskDao {
        return database.taskDao()
    }
}