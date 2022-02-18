package com.dpycb.protracker.presentation

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.dpycb.protracker.ProTrackerApp
import com.dpycb.protracker.R
import com.dpycb.protracker.databinding.ActivityMainBinding
import com.dpycb.protracker.di.DaggerMainActivityComponent
import com.dpycb.protracker.di.MainActivityComponent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var activityComponent: MainActivityComponent? = null

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = DaggerMainActivityComponent.builder().appComponent((application as ProTrackerApp).getAppComponent()).build()
        activityComponent?.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onDestroy() {
        super.onDestroy()
        activityComponent = null
    }

    fun getActivityComponent() = activityComponent!!

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return injector
    }
}