package com.gearboard.data.local.converter

import androidx.room.TypeConverter
import com.gearboard.domain.model.BoardState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room TypeConverters for serializing complex objects to/from JSON.
 */
class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromBoardState(state: BoardState): String {
        return gson.toJson(state)
    }

    @TypeConverter
    fun toBoardState(json: String): BoardState {
        return try {
            gson.fromJson(json, BoardState::class.java) ?: BoardState()
        } catch (e: Exception) {
            BoardState()
        }
    }
}
