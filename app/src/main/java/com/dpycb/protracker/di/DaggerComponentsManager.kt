package com.dpycb.protracker.di

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.dpycb.protracker.MainActivity

class DaggerComponentsManager private constructor() {
    companion object {
        val instance: DaggerComponentsManager by lazy { DaggerComponentsManager() }
    }
    private var appComponent: AppComponent? = null
    private var mainActivityComponent: MainActivityComponent? = null

    fun initAppComponent(context: Context) {
        appComponent = DaggerAppComponent
            .builder()
            .context(context)
            .build()
    }

    fun initMainActivityComponent(activity: MainActivity) {
        if (mainActivityComponent != null)
            return

        mainActivityComponent = DaggerMainActivityComponent.builder().appComponent(appComponent).build()
        mainActivityComponent?.inject(activity)
        activity.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                mainActivityComponent = null
                activity.lifecycle.removeObserver(this)
            }
        })
    }
}