package com.gearboard.data.local.converter

import androidx.room.TypeConverter
import com.gearboard.domain.model.BoardState
import com.gearboard.domain.model.ControlType
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/**
 * Room TypeConverters for serializing complex objects to/from JSON.
 * Includes a custom adapter for ControlType sealed class serialization.
 */
class Converters {

    private val gson: Gson = GsonBuilder()
        .registerTypeHierarchyAdapter(ControlType::class.java, ControlTypeAdapter())
        .create()

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

    /**
     * Gson adapter that serializes ControlType sealed class with a "type" discriminator field.
     */
    private class ControlTypeAdapter : JsonSerializer<ControlType>, JsonDeserializer<ControlType> {

        override fun serialize(src: ControlType, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            val jsonObj = Gson().toJsonTree(src).asJsonObject
            jsonObj.addProperty("_type", src::class.simpleName)
            return jsonObj
        }

        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ControlType {
            val jsonObj = json.asJsonObject
            val typeName = jsonObj.get("_type")?.asString ?: "Knob"
            val clazz = when (typeName) {
                "Knob" -> ControlType.Knob::class.java
                "Toggle" -> ControlType.Toggle::class.java
                "Tap" -> ControlType.Tap::class.java
                "Selector" -> ControlType.Selector::class.java
                "Fader" -> ControlType.Fader::class.java
                "PresetNav" -> ControlType.PresetNav::class.java
                "Pad" -> ControlType.Pad::class.java
                else -> ControlType.Knob::class.java
            }
            return Gson().fromJson(jsonObj, clazz)
        }
    }
}
