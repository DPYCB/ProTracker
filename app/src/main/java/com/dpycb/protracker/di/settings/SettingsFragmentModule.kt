package com.dpycb.protracker.di.settings

import com.dpycb.protracker.presentation.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsFragmentModule() {
    @ContributesAndroidInjector
    abstract fun getSettingsFragment(): SettingsFragment
}