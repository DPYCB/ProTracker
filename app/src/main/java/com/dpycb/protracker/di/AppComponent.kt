package com.dpycb.protracker.di

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dpycb.protracker.data.RoomDb
import com.dpycb.protracker.data.TaskDao
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [AppModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder{
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

    fun getAppDb(): RoomDb
}

@Module
class AppModule {
    @Provides
    fun provideAppDatabase(context: Context): RoomDb {
        return Room.databaseBuilder(
            context,
            RoomDb::class.java,
            "protracker_room-db"
        ).build()
    }
}