package com.dpycb.tasks.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dpycb.tasks.presentation.*
import com.dpycb.utils.di.DaggerViewModelFactory
import com.dpycb.utils.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

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
interface TasksBindsModule {
    @Binds
    fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

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