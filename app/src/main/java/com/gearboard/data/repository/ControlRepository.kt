package com.gearboard.data.repository

import com.gearboard.data.local.dao.ControlItemDao
import com.gearboard.data.local.entity.ControlItemEntity
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.ControlSize
import com.gearboard.domain.model.DisplayFormat
import com.gearboard.domain.model.FaderOrientation
import com.gearboard.domain.model.PresetNavMode
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

    /** Upsert (insert-or-replace) a list of control items by stableId. */
    suspend fun upsertAll(items: List<ControlItemEntity>) = controlItemDao.upsertAll(items)

    /** Delete all control items whose stableId is not in [activeIds]. */
    suspend fun deleteOrphans(activeIds: List<String>) = controlItemDao.deleteOrphans(activeIds)

    // --- Domain ↔ Entity conversion ---

    /** Convert a domain [ControlType] to a Room entity. */
    fun toEntity(
        control: ControlType,
        sectionType: SectionType,
        blockId: String,
        sortOrder: Int,
        entityId: Long = 0,
        blockName: String = ""
    ): ControlItemEntity {
        val (typeName, ccNumber, midiChannel, extraConfig) = extractEntityFields(control)
        return ControlItemEntity(
            id = entityId,
            stableId = control.id,
            sectionType = sectionType.name,
            blockId = blockId,
            blockName = blockName,
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
                val size = parseControlSize(extra["size"] as? String)
                ControlType.Knob(
                    id = entity.stableId,
                    label = entity.label,
                    ccNumber = entity.ccNumber,
                    midiChannel = entity.midiChannel,
                    size = size,
                    defaultValue = (extra["defaultValue"] as? Double)?.toFloat() ?: 0.5f,
                    value = (extra["value"] as? Double)?.toFloat() ?: 0.5f,
                    displayFormat = parseDisplayFormat(extra["displayFormat"] as? String),
                    ccNumberA = (extra["ccNumberA"] as? Double)?.toInt() ?: entity.ccNumber,
                    ccNumberB = (extra["ccNumberB"] as? Double)?.toInt() ?: entity.ccNumber
                )
            }
            "TOGGLE" -> {
                val extra = parseExtra(entity.extraConfig)
                val size = parseControlSize(extra["size"] as? String)
                ControlType.Toggle(
                    id = entity.stableId,
                    label = entity.label,
                    ccNumber = entity.ccNumber,
                    midiChannel = entity.midiChannel,
                    size = size,
                    momentaryMode = extra["momentaryMode"] as? Boolean ?: false,
                    isOn = extra["isOn"] as? Boolean ?: false,
                    isStompButton = extra["isStompButton"] as? Boolean ?: false,
                    ccNumberA = (extra["ccNumberA"] as? Double)?.toInt() ?: entity.ccNumber,
                    ccNumberB = (extra["ccNumberB"] as? Double)?.toInt() ?: entity.ccNumber
                )
            }
            "TAP" -> {
                val extra = parseExtra(entity.extraConfig)
                val size = parseControlSize(extra["size"] as? String)
                ControlType.Tap(
                    id = entity.stableId,
                    label = entity.label,
                    ccNumber = entity.ccNumber,
                    midiChannel = entity.midiChannel,
                    size = size
                )
            }
            "SELECTOR" -> {
                val extra = parseExtra(entity.extraConfig)
                val size = parseControlSize(extra["size"] as? String)
                @Suppress("UNCHECKED_CAST")
                val positions = (extra["positions"] as? List<*>)?.map { it.toString() } ?: emptyList()
                @Suppress("UNCHECKED_CAST")
                val ccValues = (extra["ccValues"] as? List<*>)?.mapNotNull { (it as? Double)?.toInt() }
                ControlType.Selector(
                    id = entity.stableId,
                    label = entity.label,
                    ccNumber = entity.ccNumber,
                    midiChannel = entity.midiChannel,
                    size = size,
                    positions = positions,
                    selectedIndex = (extra["selectedIndex"] as? Double)?.toInt() ?: 0,
                    ccValues = ccValues
                )
            }
            "FADER" -> {
                val extra = parseExtra(entity.extraConfig)
                val size = parseControlSize(extra["size"] as? String)
                ControlType.Fader(
                    id = entity.stableId,
                    label = entity.label,
                    ccNumber = entity.ccNumber,
                    midiChannel = entity.midiChannel,
                    size = size,
                    defaultValue = (extra["defaultValue"] as? Double)?.toFloat() ?: 0.5f,
                    value = (extra["value"] as? Double)?.toFloat() ?: 0.5f,
                    orientation = if (extra["orientation"] == "HORIZONTAL") FaderOrientation.HORIZONTAL else FaderOrientation.VERTICAL,
                    displayFormat = parseDisplayFormat(extra["displayFormat"] as? String),
                    ccNumberA = (extra["ccNumberA"] as? Double)?.toInt() ?: entity.ccNumber,
                    ccNumberB = (extra["ccNumberB"] as? Double)?.toInt() ?: entity.ccNumber
                )
            }
            "PRESET_NAV" -> {
                val extra = parseExtra(entity.extraConfig)
                val size = parseControlSize(extra["size"] as? String)
                ControlType.PresetNav(
                    id = entity.stableId,
                    label = entity.label,
                    ccNumber = entity.ccNumber,
                    midiChannel = entity.midiChannel,
                    size = size,
                    currentPreset = (extra["currentPreset"] as? Double)?.toInt() ?: 0,
                    navMode = try {
                        PresetNavMode.valueOf(extra["navMode"] as? String ?: "CC_INC_DEC")
                    } catch (_: IllegalArgumentException) { PresetNavMode.CC_INC_DEC },
                    pcChannel = (extra["pcChannel"] as? Double)?.toInt() ?: 1
                )
            }
            "PAD" -> {
                val extra = parseExtra(entity.extraConfig)
                val size = parseControlSize(extra["size"] as? String)
                ControlType.Pad(
                    id = entity.stableId,
                    label = entity.label,
                    noteNumber = (extra["noteNumber"] as? Double)?.toInt() ?: 0,
                    midiChannel = entity.midiChannel,
                    size = size,
                    velocity = (extra["velocity"] as? Double)?.toInt() ?: 100
                )
            }
            else -> ControlType.Knob(
                id = entity.stableId,
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
                    "size" to control.size.name,
                    "defaultValue" to control.defaultValue,
                    "value" to control.value,
                    "displayFormat" to control.displayFormat.name,
                    "ccNumberA" to control.ccNumberA,
                    "ccNumberB" to control.ccNumberB
                ))
            )
            is ControlType.Toggle -> EntityFields(
                typeName = "TOGGLE",
                ccNumber = control.ccNumber,
                midiChannel = control.midiChannel,
                extraConfig = gson.toJson(mapOf(
                    "size" to control.size.name,
                    "momentaryMode" to control.momentaryMode,
                    "isOn" to control.isOn,
                    "isStompButton" to control.isStompButton,
                    "ccNumberA" to control.ccNumberA,
                    "ccNumberB" to control.ccNumberB
                ))
            )
            is ControlType.Tap -> EntityFields(
                typeName = "TAP",
                ccNumber = control.ccNumber,
                midiChannel = control.midiChannel,
                extraConfig = gson.toJson(mapOf(
                    "size" to control.size.name
                ))
            )
            is ControlType.Selector -> EntityFields(
                typeName = "SELECTOR",
                ccNumber = control.ccNumber,
                midiChannel = control.midiChannel,
                extraConfig = gson.toJson(mapOf(
                    "size" to control.size.name,
                    "positions" to control.positions,
                    "selectedIndex" to control.selectedIndex,
                    "ccValues" to control.ccValues
                ))
            )
            is ControlType.Fader -> EntityFields(
                typeName = "FADER",
                ccNumber = control.ccNumber,
                midiChannel = control.midiChannel,
                extraConfig = gson.toJson(mapOf(
                    "size" to control.size.name,
                    "defaultValue" to control.defaultValue,
                    "value" to control.value,
                    "orientation" to control.orientation.name,
                    "displayFormat" to control.displayFormat.name,
                    "ccNumberA" to control.ccNumberA,
                    "ccNumberB" to control.ccNumberB
                ))
            )
            is ControlType.PresetNav -> EntityFields(
                typeName = "PRESET_NAV",
                ccNumber = control.ccNumber,
                midiChannel = control.midiChannel,
                extraConfig = gson.toJson(mapOf(
                    "size" to control.size.name,
                    "currentPreset" to control.currentPreset,
                    "navMode" to control.navMode.name,
                    "pcChannel" to control.pcChannel
                ))
            )
            is ControlType.Pad -> EntityFields(
                typeName = "PAD",
                ccNumber = 0,
                midiChannel = control.midiChannel,
                extraConfig = gson.toJson(mapOf(
                    "size" to control.size.name,
                    "noteNumber" to control.noteNumber,
                    "velocity" to control.velocity
                ))
            )
        }
    }

    private fun parseExtra(json: String): Map<String, Any> {
        if (json.isBlank()) return emptyMap()
        return try {
            val type = object : com.google.gson.reflect.TypeToken<Map<String, Any>>() {}.type
            gson.fromJson<Map<String, Any>>(json, type) ?: emptyMap()
        } catch (e: com.google.gson.JsonSyntaxException) {
            android.util.Log.w("GearBoard", "Failed to parse extraConfig, using defaults: ${e.message}")
            emptyMap()
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

    private fun parseControlSize(name: String?): ControlSize {
        return try {
            name?.let { ControlSize.valueOf(it) } ?: ControlSize.MEDIUM
        } catch (_: IllegalArgumentException) {
            ControlSize.MEDIUM
        }
    }

    private data class EntityFields(
        val typeName: String,
        val ccNumber: Int,
        val midiChannel: Int,
        val extraConfig: String
    )
}
