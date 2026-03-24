package com.gearboard.data.repository

import com.gearboard.data.local.dao.PresetDao
import com.gearboard.data.local.entity.PresetEntity
import com.gearboard.domain.model.BoardState
import com.gearboard.domain.model.Preset
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresetRepository @Inject constructor(
    private val presetDao: PresetDao,
    private val gson: Gson
) {
    fun getAllPresets(): Flow<List<Preset>> {
        return presetDao.getAllPresets().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getPresetById(id: Long): Preset? {
        return presetDao.getPresetById(id)?.toDomain()
    }

    suspend fun getPresetCount(): Int {
        return presetDao.getPresetCount()
    }

    suspend fun savePreset(preset: Preset): Long {
        val entity = preset.toEntity()
        return if (preset.id == 0L) {
            presetDao.insertPreset(entity)
        } else {
            presetDao.updatePreset(entity.copy(updatedAt = System.currentTimeMillis()))
            preset.id
        }
    }

    suspend fun deletePreset(preset: Preset) {
        presetDao.deletePreset(preset.toEntity())
    }

    suspend fun deleteAllPresets() {
        presetDao.deleteAllPresets()
    }

    // --- Mapping helpers ---

    private fun PresetEntity.toDomain(): Preset {
        val boardState = try {
            gson.fromJson(boardStateJson, BoardState::class.java) ?: BoardState()
        } catch (e: Exception) {
            BoardState()
        }
        return Preset(
            id = id,
            name = name,
            bank = bank,
            program = program,
            boardState = boardState,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun Preset.toEntity(): PresetEntity {
        return PresetEntity(
            id = id,
            name = name,
            bank = bank,
            program = program,
            boardStateJson = gson.toJson(boardState),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
