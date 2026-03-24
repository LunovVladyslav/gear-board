package com.gearboard.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "gearboard_settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object Keys {
        val MIDI_CHANNEL = intPreferencesKey("midi_channel")
        val SEND_ON_CHANGE = booleanPreferencesKey("send_on_change")
        val CONTROL_SIZE = floatPreferencesKey("control_size")
        val HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
        val KEEP_SCREEN_ON = booleanPreferencesKey("keep_screen_on")
        val IS_PREMIUM = booleanPreferencesKey("is_premium")
        val AUTO_RECONNECT = booleanPreferencesKey("auto_reconnect")
    }

    val midiChannel: Flow<Int> = context.dataStore.data.map { it[MIDI_CHANNEL] ?: 0 }
    val sendOnChange: Flow<Boolean> = context.dataStore.data.map { it[SEND_ON_CHANGE] ?: true }
    val controlSize: Flow<Float> = context.dataStore.data.map { it[CONTROL_SIZE] ?: 1.0f }
    val hapticEnabled: Flow<Boolean> = context.dataStore.data.map { it[HAPTIC_ENABLED] ?: true }
    val keepScreenOn: Flow<Boolean> = context.dataStore.data.map { it[KEEP_SCREEN_ON] ?: true }
    val isPremium: Flow<Boolean> = context.dataStore.data.map { it[IS_PREMIUM] ?: false }
    val autoReconnect: Flow<Boolean> = context.dataStore.data.map { it[AUTO_RECONNECT] ?: true }

    suspend fun setMidiChannel(channel: Int) {
        context.dataStore.edit { it[MIDI_CHANNEL] = channel }
    }

    suspend fun setSendOnChange(enabled: Boolean) {
        context.dataStore.edit { it[SEND_ON_CHANGE] = enabled }
    }

    suspend fun setControlSize(size: Float) {
        context.dataStore.edit { it[CONTROL_SIZE] = size.coerceIn(0.8f, 1.4f) }
    }

    suspend fun setHapticEnabled(enabled: Boolean) {
        context.dataStore.edit { it[HAPTIC_ENABLED] = enabled }
    }

    suspend fun setKeepScreenOn(enabled: Boolean) {
        context.dataStore.edit { it[KEEP_SCREEN_ON] = enabled }
    }

    suspend fun setPremium(premium: Boolean) {
        context.dataStore.edit { it[IS_PREMIUM] = premium }
    }

    suspend fun setAutoReconnect(enabled: Boolean) {
        context.dataStore.edit { it[AUTO_RECONNECT] = enabled }
    }

    suspend fun resetToDefaults() {
        context.dataStore.edit { prefs ->
            prefs[MIDI_CHANNEL] = 0
            prefs[SEND_ON_CHANGE] = true
            prefs[CONTROL_SIZE] = 1.0f
            prefs[HAPTIC_ENABLED] = true
            prefs[KEEP_SCREEN_ON] = true
            prefs[AUTO_RECONNECT] = true
            // Note: isPremium is NOT reset
        }
    }
}
