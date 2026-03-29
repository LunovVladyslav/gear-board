package com.gearboard.data.repository

import com.gearboard.data.local.dao.LiveModeDao
import com.gearboard.data.local.entity.BarEventEntity
import com.gearboard.data.local.entity.LiveSessionEntity
import com.gearboard.domain.model.BarEvent
import com.gearboard.domain.model.LiveSession
import com.gearboard.domain.model.TimeSignature
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveModeRepository @Inject constructor(
    private val dao: LiveModeDao,
    private val gson: Gson
) {
    fun getAllSessions(): Flow<List<LiveSession>> =
        dao.getAllSessions().map { it.map { e -> e.toDomain() } }

    suspend fun getSessionById(id: String): LiveSession? =
        dao.getSessionById(id)?.toDomain()

    suspend fun saveSession(session: LiveSession) {
        dao.insertSession(session.toEntity())
    }

    suspend fun deleteSession(session: LiveSession) {
        dao.deleteSession(session.toEntity())
        dao.deleteAllEventsForSession(session.id)
    }

    fun getEventsForSession(sessionId: String): Flow<List<BarEvent>> =
        dao.getEventsForSession(sessionId).map { it.map { e -> e.toDomain() } }

    suspend fun upsertEvent(event: BarEvent) {
        dao.insertEvent(event.toEntity())
    }

    suspend fun clearBar(sessionId: String, bar: Int) {
        dao.deleteEventAtBar(sessionId, bar)
    }

    // --- Mapping helpers ---

    private fun LiveSessionEntity.toDomain(): LiveSession {
        val ts = try {
            gson.fromJson(initialTimeSigJson, TimeSignature::class.java) ?: TimeSignature(4, 4)
        } catch (e: Exception) { TimeSignature(4, 4) }
        return LiveSession(
            id = id,
            name = name,
            totalBars = totalBars,
            initialBpm = initialBpm,
            initialTimeSignature = ts,
            createdAt = createdAt
        )
    }

    private fun LiveSession.toEntity(): LiveSessionEntity {
        return LiveSessionEntity(
            id = id,
            name = name,
            totalBars = totalBars,
            initialBpm = initialBpm,
            initialTimeSigJson = gson.toJson(initialTimeSignature),
            createdAt = createdAt
        )
    }

    private fun BarEventEntity.toDomain(): BarEvent {
        val ts = timeSigJson?.let {
            try { gson.fromJson(it, TimeSignature::class.java) } catch (e: Exception) { null }
        }
        return BarEvent(
            id = id,
            liveSessionId = liveSessionId,
            barNumber = barNumber,
            presetId = presetId,
            bpm = bpm,
            timeSignature = ts
        )
    }

    private fun BarEvent.toEntity(): BarEventEntity {
        return BarEventEntity(
            id = id,
            liveSessionId = liveSessionId,
            barNumber = barNumber,
            presetId = presetId,
            bpm = bpm,
            timeSigJson = timeSignature?.let { gson.toJson(it) }
        )
    }
}
