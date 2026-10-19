package io.github.juevigrace.diva.lib.file.presentation.ui.components.selection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

/**
 * UI-local state for drag selection.
 *
 * The gesture starts on a long press. That entry's resulting state becomes the
 * drag [mode] and the contiguous span between the anchor row and the row under
 * the finger is forced to it — a rubber band. Rows that leave the span fall back
 * to the selection that existed when the drag began, so dragging back over
 * entries this drag selected unselects them again.
 *
 * All bookkeeping lives here and is discarded when the drag ends; the view model
 * only ever sees plain toggles through the `onToggle` callbacks.
 */
class DragSelectionState {
    var mode: Boolean? by mutableStateOf(null)
        private set

    /** True while a drag selection is in progress. */
    val dragging: Boolean get() = mode != null

    /**
     * Node-local position of the finger during a drag, published for the
     * auto-scroll loop that runs outside the pointer scope.
     */
    var fingerPosition: Offset? by mutableStateOf(null)

    private var anchorIndex = -1
    private var edgeIndex = -1

    /** Listing order the band is computed against, fixed at drag start. */
    private var orderedIds: List<String> = emptyList()

    /** Selection snapshot taken when the drag began. */
    private var baseline: Set<String> = emptySet()

    /** Mirror of the selection as this drag has left it. */
    private val current = mutableSetOf<String>()

    /** Row bounds in root coordinates, keyed by entry id. */
    private val rowBounds = mutableMapOf<String, Rect>()

    fun updateBounds(entryId: String, bounds: Rect) {
        rowBounds[entryId] = bounds
    }

    fun removeBounds(entryId: String) {
        rowBounds.remove(entryId)
    }

    /** Entry whose row contains [position] (root coordinates), or null. */
    fun hitTest(position: Offset): String? =
        rowBounds.entries.firstOrNull { it.value.contains(position) }?.key

    /**
     * Begins a drag on the entry under [position] (root coordinates). The anchor
     * is toggled once and the resulting state decides whether the rest of the
     * drag selects or unselects. [orderedIds] is the listing order the band is
     * computed in and [selectedIds] the selection snapshot at press time.
     *
     * @return true when a drag was started.
     */
    fun start(
        position: Offset,
        orderedIds: List<String>,
        selectedIds: Set<String>,
        onToggle: (String) -> Unit,
    ): Boolean {
        val anchor = hitTest(position) ?: return false
        val index = orderedIds.indexOf(anchor)
        if (index < 0) return false

        reset()
        this.orderedIds = orderedIds
        anchorIndex = index
        edgeIndex = index
        baseline = selectedIds
        current += selectedIds
        mode = anchor !in current
        applyBand(onToggle)
        return true
    }

    /** Moves the band edge to the entry under [position] (root coordinates). */
    fun moveTo(position: Offset, onToggle: (String) -> Unit) {
        if (mode == null) return
        val hit = hitTest(position) ?: return
        val index = orderedIds.indexOf(hit)
        // A gap between rows, a sticky header or a row scrolled out of the cache
        // leaves the band where it is instead of jumping.
        if (index < 0 || index == edgeIndex) return
        edgeIndex = index
        applyBand(onToggle)
    }

    /** Ends the drag, keeping the selection it produced. */
    fun finish() = reset()

    /** Ends the drag and rolls the selection back to the pre-drag snapshot. */
    fun cancel(onToggle: (String) -> Unit) {
        (baseline + current).forEach { id ->
            if ((id in current) != (id in baseline)) onToggle(id)
        }
        reset()
    }

    private fun applyBand(onToggle: (String) -> Unit) {
        val select = mode ?: return
        val from = minOf(anchorIndex, edgeIndex)
        val to = maxOf(anchorIndex, edgeIndex)
        orderedIds.forEachIndexed { index, id ->
            val target = if (index in from..to) select else id in baseline
            if (target == (id in current)) return@forEachIndexed
            if (target) current += id else current -= id
            onToggle(id)
        }
    }

    private fun reset() {
        mode = null
        fingerPosition = null
        anchorIndex = -1
        edgeIndex = -1
        orderedIds = emptyList()
        baseline = emptySet()
        current.clear()
    }
}
