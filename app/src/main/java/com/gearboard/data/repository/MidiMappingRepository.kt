package com.gearboard.data.repository

import com.gearboard.data.local.dao.MidiMappingDao
import com.gearboard.data.local.entity.MidiMappingEntity
import com.gearboard.domain.model.MidiMapping
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MidiMappingRepository @Inject constructor(
    private val midiMappingDao: MidiMappingDao
) {
    fun getAllMappings(): Flow<List<MidiMapping>> {
        return midiMappingDao.getAllMappings().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getMappingByControlId(controlId: String): MidiMapping? {
        return midiMappingDao.getMappingByControlId(controlId)?.toDomain()
    }

    suspend fun saveMapping(mapping: MidiMapping): Long {
        val entity = mapping.toEntity()
        return if (mapping.id == 0L) {
            midiMappingDao.insertMapping(entity)
        } else {
            midiMappingDao.updateMapping(entity)
            mapping.id
        }
    }

    suspend fun deleteMapping(controlId: String) {
        midiMappingDao.deleteMappingByControlId(controlId)
    }

    suspend fun deleteAllMappings() {
        midiMappingDao.deleteAllMappings()
    }

    // --- Mapping helpers ---

    private fun MidiMappingEntity.toDomain(): MidiMapping {
        return MidiMapping(
            id = id,
            controlId = controlId,
            controlName = controlName,
            ccNumber = ccNumber,
            channel = channel
        )
    }

    private fun MidiMapping.toEntity(): MidiMappingEntity {
        return MidiMappingEntity(
            id = id,
            controlId = controlId,
            controlName = controlName,
            ccNumber = ccNumber,
            channel = channel
        )
    }
}
