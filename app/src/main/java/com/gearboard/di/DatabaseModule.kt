package com.gearboard.di

import android.content.Context
import androidx.room.Room
import com.gearboard.data.local.GearBoardDatabase
import com.gearboard.data.local.dao.MidiMappingDao
import com.gearboard.data.local.dao.PresetDao
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GearBoardDatabase {
        return Room.databaseBuilder(
            context,
            GearBoardDatabase::class.java,
            "gearboard_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePresetDao(database: GearBoardDatabase): PresetDao {
        return database.presetDao()
    }

    @Provides
    fun provideMidiMappingDao(database: GearBoardDatabase): MidiMappingDao {
        return database.midiMappingDao()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }
}
