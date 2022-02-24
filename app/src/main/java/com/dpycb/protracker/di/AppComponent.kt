package com.dpycb.protracker.di

import android.content.Context
import androidx.room.Room
import com.dpycb.protracker.data.RoomDb
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