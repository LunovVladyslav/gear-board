package com.gearboard.domain.model

/**
 * Immutable command describing a reversible board action.
 * Used by [com.gearboard.domain.UndoRedoManager] to support undo/redo.
 */
sealed class BoardCommand {

    data class ControlValueChanged(
        val blockId: String,
        val section: SectionType,
        val controlId: String,
        val oldValue: Float,
        val newValue: Float
    ) : BoardCommand()

    data class ControlAdded(
        val blockId: String,
        val section: SectionType,
        val control: ControlType
    ) : BoardCommand()

    data class ControlRemoved(
        val blockId: String,
        val section: SectionType,
        val control: ControlType,
        val position: Int
    ) : BoardCommand()

    data class BlockAdded(
        val section: SectionType,
        val block: Any  // ControlBlock | AmpBlock | CabBlock
    ) : BoardCommand()

    data class BlockRemoved(
        val section: SectionType,
        val block: Any,
        val position: Int
    ) : BoardCommand()

    data class BlockRenamed(
        val blockId: String,
        val section: SectionType,
        val oldName: String,
        val newName: String
    ) : BoardCommand()

    data class AbSlotSaved(
        val blockId: String,
        val section: SectionType,
        val slot: AbSlot,
        val oldStates: Map<String, Float>,
        val newStates: Map<String, Float>
    ) : BoardCommand()
}
