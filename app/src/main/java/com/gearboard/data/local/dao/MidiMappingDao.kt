package com.gearboard.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gearboard.data.local.entity.MidiMappingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MidiMappingDao {

    @Query("SELECT * FROM midi_mappings ORDER BY controlName ASC")
    fun getAllMappings(): Flow<List<MidiMappingEntity>>

    @Query("SELECT * FROM midi_mappings WHERE controlId = :controlId LIMIT 1")
    suspend fun getMappingByControlId(controlId: String): MidiMappingEntity?

    @Query("SELECT * FROM midi_mappings WHERE ccNumber = :ccNumber LIMIT 1")
    suspend fun getMappingByCcNumber(ccNumber: Int): MidiMappingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMapping(mapping: MidiMappingEntity): Long

    @Update
    suspend fun updateMapping(mapping: MidiMappingEntity)

    @Delete
    suspend fun deleteMapping(mapping: MidiMappingEntity)

    @Query("DELETE FROM midi_mappings WHERE controlId = :controlId")
    suspend fun deleteMappingByControlId(controlId: String)

    @Query("DELETE FROM midi_mappings")
    suspend fun deleteAllMappings()
}
