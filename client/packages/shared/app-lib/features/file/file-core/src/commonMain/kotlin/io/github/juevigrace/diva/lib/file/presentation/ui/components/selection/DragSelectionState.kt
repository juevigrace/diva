package io.github.juevigrace.diva.lib.file.presentation.ui.components.selection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

/**
 * UI-local state for drag selection.
 *
 * The gesture starts on a long press: that entry is toggled and its resulting
 * state becomes the drag [mode] — if it was unselected the drag selects every
 * entry it crosses, if it was selected the drag unselects them. Entries that
 * already match the mode are left untouched, so passing back over an entry
 * never flips it twice.
 *
 * All bookkeeping lives here and is discarded by [reset] when the drag ends;
 * the view model only ever sees plain toggles.
 */
class DragSelectionState {
    var mode: Boolean? by mutableStateOf(null)
        private set

    var startId: String? by mutableStateOf(null)
        private set

    /** Snapshot of the selection at drag start, updated as events are emitted. */
    private val sessionSelected = mutableSetOf<String>()

    /** Entries already acted on during this drag. */
    private val handled = mutableSetOf<String>()

    /** Row bounds in root coordinates, keyed by entry id. */
    private val rowBounds = mutableMapOf<String, Rect>()

    fun updateBounds(entryId: String, bounds: Rect) {
        rowBounds[entryId] = bounds
    }

    fun removeBounds(entryId: String) {
        rowBounds.remove(entryId)
    }

    fun boundsOf(entryId: String): Rect? = rowBounds[entryId]

    fun hitTest(position: Offset): String? =
        rowBounds.entries.firstOrNull { it.value.contains(position) }?.key

    /**
     * Begins a drag on [entryId]. The entry is toggled immediately and the
     * resulting state decides whether the rest of the drag selects or
     * unselects. [selectedIds] must be the selection snapshot at press time.
     */
    fun start(entryId: String, selectedIds: Set<String>, onToggle: (String) -> Unit) {
        reset()
        sessionSelected.addAll(selectedIds)
        startId = entryId
        val select = entryId !in sessionSelected
        mode = select
        handled += entryId
        apply(entryId, select, onToggle)
    }

    /** Applies the active drag mode to [entryId] if it has not been handled yet. */
    fun moveTo(entryId: String, onToggle: (String) -> Unit) {
        val select = mode ?: return
        if (!handled.add(entryId)) return
        apply(entryId, select, onToggle)
    }

    fun reset() {
        mode = null
        startId = null
        handled.clear()
        sessionSelected.clear()
    }

    private fun apply(entryId: String, select: Boolean, onToggle: (String) -> Unit) {
        val isSelected = entryId in sessionSelected
        if (select == isSelected) return
        if (select) sessionSelected += entryId else sessionSelected -= entryId
        onToggle(entryId)
    }
}
