package com.gearboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "live_sessions")
data class LiveSessionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val totalBars: Int,
    val initialBpm: Float,
    val initialTimeSigJson: String,   // JSON: {"numerator":4,"denominator":4}
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bar_events")
data class BarEventEntity(
    @PrimaryKey val id: String,
    val liveSessionId: String,
    val barNumber: Int,
    val presetId: Long? = null,
    val bpm: Float? = null,
    val timeSigJson: String? = null   // JSON or null
)
