package com.dpycb.protracker

import android.app.Application
import androidx.room.Room
import com.dpycb.protracker.data.RoomDb
import com.dpycb.protracker.di.AppComponent
import com.dpycb.protracker.di.DaggerAppComponent

class ProTrackerApp : Application() {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .context(this)
            .build()
    }

    fun getAppComponent(): AppComponent = appComponent
}