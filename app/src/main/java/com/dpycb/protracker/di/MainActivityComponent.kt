package com.dpycb.protracker.di

import com.dpycb.protracker.MainActivity
import com.dpycb.protracker.di.settings.SettingsFragmentModule
import com.dpycb.protracker.di.tasks.TasksProvidesModule
import com.dpycb.tasks.di.TaskFragmentModule
import com.dpycb.tasks.di.TasksBindsModule
import com.dpycb.utils.di.ActivityScope
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [
        AndroidSupportInjectionModule::class,
        TaskFragmentModule::class,
        SettingsFragmentModule::class,
        TasksBindsModule::class,
        TasksProvidesModule::class
    ]
)
interface MainActivityComponent {
    fun inject(activity: MainActivity)
}