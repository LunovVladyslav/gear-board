package com.gearboard.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val midiChannel = settingsRepository.midiChannel
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val sendOnChange = settingsRepository.sendOnChange
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val controlSize = settingsRepository.controlSize
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1.0f)

    val hapticEnabled = settingsRepository.hapticEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val keepScreenOn = settingsRepository.keepScreenOn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val isPremium = settingsRepository.isPremium
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val autoReconnect = settingsRepository.autoReconnect
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun setMidiChannel(ch: Int) = viewModelScope.launch { settingsRepository.setMidiChannel(ch) }
    fun setSendOnChange(v: Boolean) = viewModelScope.launch { settingsRepository.setSendOnChange(v) }
    fun setControlSize(s: Float) = viewModelScope.launch { settingsRepository.setControlSize(s) }
    fun setHapticEnabled(v: Boolean) = viewModelScope.launch { settingsRepository.setHapticEnabled(v) }
    fun setKeepScreenOn(v: Boolean) = viewModelScope.launch { settingsRepository.setKeepScreenOn(v) }
    fun setAutoReconnect(v: Boolean) = viewModelScope.launch { settingsRepository.setAutoReconnect(v) }
    fun resetToDefaults() = viewModelScope.launch { settingsRepository.resetToDefaults() }
}
