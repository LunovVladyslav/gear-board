package com.gearboard.di

import android.content.Context
import android.media.midi.MidiManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MidiModule {

    @Provides
    @Singleton
    fun provideMidiManager(@ApplicationContext context: Context): MidiManager {
        return context.getSystemService(Context.MIDI_SERVICE) as MidiManager
    }
}
