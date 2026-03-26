package com.gearboard.data.repository

import com.gearboard.data.local.dao.ControlItemDao
import com.gearboard.data.local.entity.ControlItemEntity
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.DisplayFormat
import com.gearboard.domain.model.FaderOrientation
import com.gearboard.domain.model.SectionType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ControlRepository — manages persisted control items.
 * Converts between domain [ControlType] models and Room [ControlItemEntity].
 */
@Singleton
class ControlRepository @Inject constructor(
    private val controlItemDao: ControlItemDao,
    private val gson: Gson
) {
    /** All controls as a reactive stream, grouped by blockId. */
    fun getAllControls(): Flow<List<ControlItemEntity>> = controlItemDao.getAllControls()

    /** Controls for a specific section as a reactive stream. */
    fun getBySection(sectionType: SectionType): Flow<List<ControlItemEntity>> =
        controlItemDao.getBySection(sectionType.name)

    /** Controls for a specific block as a reactive stream. */
    fun getByBlock(blockId: String): Flow<List<ControlItemEntity>> =
        controlItemDao.getByBlock(blockId)

    /** Get all controls grouped by section and block (one-shot). */
    suspend fun getAllControlsOnce(): List<ControlItemEntity> =
        controlItemDao.getAllControlsOnce()

    /** Get controls for a section (one-shot). */
    suspend fun getBySectionOnce(sectionType: SectionType): List<ControlItemEntity> =
        controlItemDao.getBySectionOnce(sectionType.name)

    /** Insert a single control item. */
    suspend fun insert(item: ControlItemEntity): Long =
        controlItemDao.insert(item)

    /** Insert multiple control items. */
    suspend fun insertAll(items: List<ControlItemEntity>) =
        controlItemDao.insertAll(items)

    /** Update a control item. */
    suspend fun update(item: ControlItemEntity) =
        controlItemDao.update(item)

    /** Delete a control item. */
    suspend fun delete(item: ControlItemEntity) =
        controlItemDao.delete(item)

    /** Delete all controls in a block. */
    suspend fun deleteByBlock(blockId: String) =
        controlItemDao.deleteByBlock(blockId)

    /** Delete all controls in a section. */
    suspend fun deleteBySection(sectionType: SectionType) =
        controlItemDao.deleteBySection(sectionType.name)

    /** Delete all controls. */
    suspend fun deleteAll() = controlItemDao.deleteAll()

    /** Check if any controls exist. */
    suspend fun isEmpty(): Boolean = controlItemDao.count() == 0

    // --- Domain ↔ Entity conversion ---

    /** Convert a domain [ControlType] to a Room entity. */
    fun toEntity(
        control: ControlType,
        sectionType: SectionType,
        blockId: String,
        sortOrder: Int,
        entityId: Long = 0
    ): ControlItemEntity {
        val (typeName, ccNumber, midiChannel, extraConfig) = extractEntityFields(control)
        return ControlItemEntity(
            id = entityId,
            sectionType = sectionType.name,
            blockId = blockId,
            controlType = typeName,
            label = control.label,
            ccNumber = ccNumber,
            midiChannel = midiChannel,
            extraConfig = extraConfig,
            sortOrder = sortOrder
        )
    }

    /** Convert a Room entity back to a domain [ControlType]. */
    fun toDomain(entity: ControlItemEntity): ControlType {
        return when (entity.controlType) {
            "KNOB" -> {
                val extra = parseExtra(entity.extraConfig)
                ControlType.Knob(
                    id = entity.id.toString(),
                    label = entity.label,
                    ccNumber = entity.ccNumber,
                    midiChannel = entity.midiChannel,
                    defaultValue = (extra["defaultValue"] as? Double)?.toFloat() ?: 0.5f,
                    value = (extra["value"] as? Double)?.toFloat() ?: 0.5f,
                    displayFormat = parseDisplayFormat(extra["displayFormat"] as? String)
                )
            }
            "TOGGLE" -> {
                val extra = parseExtra(entity.extraConfig)
                ControlType.Toggle(
                    id = entity.id.toString(),
                    label = entity.label,
                    ccNumber = entity.ccNumber,
                    midiChannel = entity.midiChannel,
                    pulseMode = extra["pulseMode"] as? Boolean ?: true,
                    isOn = extra["isOn"] as? Boolean ?: false
                )
            }
            "TAP" -> ControlType.Tap(
                id = entity.id.toString(),
                label = entity.label,
                ccNumber = entity.ccNumber,
                midiChannel = entity.midiChannel
            )
            "SELECTOR" -> {
                val extra = parseExtra(entity.extraConfig)
                @Suppress("UNCHECKED_CAST")
                val positions = (extra["positions"] as? List<*>)?.map { it.toString() } ?: emptyList()
                ControlType.Selector(
                    id = entity.id.toString(),
                    label = entity.label,
                    ccNumber = entity.ccNumber,
                    midiChannel = entity.midiChannel,
                    positions = positions,
                    selectedIndex = (extra["selectedIndex"] as? Double)?.toInt() ?: 0
                )
            }
            "FADER" -> {
                val extra = parseExtra(entity.extraConfig)
                ControlType.Fader(
                    id = entity.id.toString(),
                    label = entity.label,
                    ccNumber = entity.ccNumber,
                    midiChannel = entity.midiChannel,
                    defaultValue = (extra["defaultValue"] as? Double)?.toFloat() ?: 0.5f,
                    value = (extra["value"] as? Double)?.toFloat() ?: 0.5f,
                    orientation = if (extra["orientation"] == "HORIZONTAL") FaderOrientation.HORIZONTAL else FaderOrientation.VERTICAL,
                    displayFormat = parseDisplayFormat(extra["displayFormat"] as? String)
                )
            }
            "PRESET_NAV" -> {
                val extra = parseExtra(entity.extraConfig)
                ControlType.PresetNav(
                    id = entity.id.toString(),
                    label = entity.label,
                    midiChannel = entity.midiChannel,
                    currentPreset = (extra["currentPreset"] as? Double)?.toInt() ?: 0
                )
            }
            "PAD" -> {
                val extra = parseExtra(entity.extraConfig)
                ControlType.Pad(
                    id = entity.id.toString(),
                    label = entity.label,
                    noteNumber = (extra["noteNumber"] as? Double)?.toInt() ?: 0,
                    midiChannel = entity.midiChannel,
                    velocity = (extra["velocity"] as? Double)?.toInt() ?: 100
                )
            }
            else -> ControlType.Knob(
                id = entity.id.toString(),
                label = entity.label,
                ccNumber = entity.ccNumber,
                midiChannel = entity.midiChannel
            )
        }
    }

    private fun extractEntityFields(control: ControlType): EntityFields {
        return when (control) {
            is ControlType.Knob -> EntityFields(
                typeName = "KNOB",
                ccNumber = control.ccNumber,
                midiChannel = control.midiChannel,
                extraConfig = gson.toJson(mapOf(
                    "defaultValue" to control.defaultValue,
                    "value" to control.value,
                    "displayFormat" to control.displayFormat.name
                ))
            )
            is ControlType.Toggle -> EntityFields(
                typeName = "TOGGLE",
                ccNumber = control.ccNumber,
                midiChannel = control.midiChannel,
                extraConfig = gson.toJson(mapOf(
                    "pulseMode" to control.pulseMode,
                    "isOn" to control.isOn
                ))
            )
            is ControlType.Tap -> EntityFields(
                typeName = "TAP",
                ccNumber = control.ccNumber,
                midiChannel = control.midiChannel,
                extraConfig = ""
            )
            is ControlType.Selector -> EntityFields(
                typeName = "SELECTOR",
                ccNumber = control.ccNumber,
                midiChannel = control.midiChannel,
                extraConfig = gson.toJson(mapOf(
                    "positions" to control.positions,
                    "selectedIndex" to control.selectedIndex
                ))
            )
            is ControlType.Fader -> EntityFields(
                typeName = "FADER",
                ccNumber = control.ccNumber,
                midiChannel = control.midiChannel,
                extraConfig = gson.toJson(mapOf(
                    "defaultValue" to control.defaultValue,
                    "value" to control.value,
                    "orientation" to control.orientation.name,
                    "displayFormat" to control.displayFormat.name
                ))
            )
            is ControlType.PresetNav -> EntityFields(
                typeName = "PRESET_NAV",
                ccNumber = 0,
                midiChannel = control.midiChannel,
                extraConfig = gson.toJson(mapOf(
                    "currentPreset" to control.currentPreset
                ))
            )
            is ControlType.Pad -> EntityFields(
                typeName = "PAD",
                ccNumber = 0,
                midiChannel = control.midiChannel,
                extraConfig = gson.toJson(mapOf(
                    "noteNumber" to control.noteNumber,
                    "velocity" to control.velocity
                ))
            )
        }
    }

    private fun parseExtra(json: String): Map<String, Any> {
        if (json.isBlank()) return emptyMap()
        return try {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            gson.fromJson(json, type) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    private fun parseDisplayFormat(name: String?): DisplayFormat {
        return try {
            name?.let { DisplayFormat.valueOf(it) } ?: DisplayFormat.ZERO_TO_TEN
        } catch (_: IllegalArgumentException) {
            DisplayFormat.ZERO_TO_TEN
        }
    }

    private data class EntityFields(
        val typeName: String,
        val ccNumber: Int,
        val midiChannel: Int,
        val extraConfig: String
    )
}
