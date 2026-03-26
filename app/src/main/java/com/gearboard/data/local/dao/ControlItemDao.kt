package com.gearboard.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gearboard.data.local.entity.ControlItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ControlItemDao {

    @Query("SELECT * FROM control_items ORDER BY sectionType, blockId, sortOrder")
    fun getAllControls(): Flow<List<ControlItemEntity>>

    @Query("SELECT * FROM control_items WHERE sectionType = :sectionType ORDER BY blockId, sortOrder")
    fun getBySection(sectionType: String): Flow<List<ControlItemEntity>>

    @Query("SELECT * FROM control_items WHERE blockId = :blockId ORDER BY sortOrder")
    fun getByBlock(blockId: String): Flow<List<ControlItemEntity>>

    @Query("SELECT * FROM control_items WHERE sectionType = :sectionType ORDER BY blockId, sortOrder")
    suspend fun getBySectionOnce(sectionType: String): List<ControlItemEntity>

    @Query("SELECT * FROM control_items ORDER BY sectionType, blockId, sortOrder")
    suspend fun getAllControlsOnce(): List<ControlItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ControlItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ControlItemEntity>)

    @Update
    suspend fun update(item: ControlItemEntity)

    @Delete
    suspend fun delete(item: ControlItemEntity)

    @Query("DELETE FROM control_items WHERE blockId = :blockId")
    suspend fun deleteByBlock(blockId: String)

    @Query("DELETE FROM control_items WHERE sectionType = :sectionType")
    suspend fun deleteBySection(sectionType: String)

    @Query("DELETE FROM control_items")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM control_items")
    suspend fun count(): Int
}
