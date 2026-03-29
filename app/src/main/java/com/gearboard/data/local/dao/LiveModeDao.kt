package com.gearboard.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gearboard.data.local.entity.BarEventEntity
import com.gearboard.data.local.entity.LiveSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LiveModeDao {

    // --- Sessions ---
    @Query("SELECT * FROM live_sessions ORDER BY createdAt DESC")
    fun getAllSessions(): Flow<List<LiveSessionEntity>>

    @Query("SELECT * FROM live_sessions WHERE id = :id")
    suspend fun getSessionById(id: String): LiveSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: LiveSessionEntity)

    @Update
    suspend fun updateSession(session: LiveSessionEntity)

    @Delete
    suspend fun deleteSession(session: LiveSessionEntity)

    // --- Bar events ---
    @Query("SELECT * FROM bar_events WHERE liveSessionId = :sessionId ORDER BY barNumber ASC")
    fun getEventsForSession(sessionId: String): Flow<List<BarEventEntity>>

    @Query("SELECT * FROM bar_events WHERE liveSessionId = :sessionId AND barNumber = :bar LIMIT 1")
    suspend fun getEventAtBar(sessionId: String, bar: Int): BarEventEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: BarEventEntity)

    @Delete
    suspend fun deleteEvent(event: BarEventEntity)

    @Query("DELETE FROM bar_events WHERE liveSessionId = :sessionId AND barNumber = :bar")
    suspend fun deleteEventAtBar(sessionId: String, bar: Int)

    @Query("DELETE FROM bar_events WHERE liveSessionId = :sessionId")
    suspend fun deleteAllEventsForSession(sessionId: String)
}
