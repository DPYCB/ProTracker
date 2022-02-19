package com.dpycb.protracker.di

import androidx.lifecycle.ViewModel
import com.dpycb.protracker.data.RoomDb
import com.dpycb.protracker.data.TaskDao
import com.dpycb.protracker.data.TasksRepository
import com.dpycb.protracker.domain.ITaskRepository
import com.dpycb.protracker.presentation.*
import dagger.*
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [
        AndroidSupportInjectionModule::class,
        TaskFragmentModule::class,
        SettingsFragmentModule::class,
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
    abstract fun getTaskFragment(): TaskFragment

    @ContributesAndroidInjector
    abstract fun getAddTaskFragment(): AddTaskFragment

    @ContributesAndroidInjector
    abstract fun getTaskDetailsFragment(): TaskDetailsFragment
}

@Module
abstract class SettingsFragmentModule() {
    @ContributesAndroidInjector
    abstract fun getSettingsFragment(): SettingsFragment
}

@Module
interface MainActivityBindsModule {
    @Binds
    @[IntoMap ViewModelKey(TaskFragmentViewModel::class)]
    fun getTaskViewModel(viewModel: TaskFragmentViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(AddTaskViewModel::class)]
    fun getAddTaskViewModel(viewModel: AddTaskViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(TaskDetailsViewModel::class)]
    fun getTaskDetailsViewModel(viewModel: TaskDetailsViewModel): ViewModel
}

@Module
class TasksModule {
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