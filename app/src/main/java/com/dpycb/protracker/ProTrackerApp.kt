package com.dpycb.protracker

import android.app.Application
import androidx.room.Room
import com.dpycb.protracker.data.RoomDb
import com.dpycb.protracker.di.AppComponent
import com.dpycb.protracker.di.DaggerAppComponent
import com.dpycb.protracker.di.DaggerComponentsManager

class ProTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DaggerComponentsManager.instance.initAppComponent(this)
    }
}