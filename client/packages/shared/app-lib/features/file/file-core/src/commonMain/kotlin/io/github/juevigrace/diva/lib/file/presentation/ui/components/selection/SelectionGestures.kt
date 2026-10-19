package io.github.juevigrace.diva.lib.file.presentation.ui.components.selection

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned

/**
 * Row-level gesture wiring for the files list.
 *
 * A single detector drives both interaction modes:
 *
 * - A plain tap is forwarded to [onTap]; the screen decides whether it opens a
 *   directory or toggles selection.
 * - Holding past the long-press timeout starts a drag selection: the entry is
 *   toggled once through [DragSelectionState.start] and every entry the finger
 *   then crosses is fed to [onToggle] until it lifts.
 *
 * A press that a descendant already claimed (a checkbox tap) or that turns into
 * a scroll is left alone, so selection gestures never fight the list. The row's
 * bounds are registered in root coordinates for cross-row hit-testing and
 * dropped when the row leaves composition.
 */
@Composable
fun Modifier.selectionEntry(
    entryId: String,
    dragState: DragSelectionState,
    selectedIds: () -> Set<String>,
    onTap: (String) -> Unit,
    onToggle: (String) -> Unit,
): Modifier {
    DisposableEffect(entryId) {
        onDispose { dragState.removeBounds(entryId) }
    }

    return this
        .onGloballyPositioned { dragState.updateBounds(entryId, it.boundsInRoot()) }
        .pointerInput(entryId) {
            awaitEachGesture {
                // requireUnconsumed: a checkbox press is consumed by the checkbox
                // itself, so the row never sees that down and stays out of it.
                val down = awaitFirstDown()
                val downPosition = down.position

                // Wait out the press: it either lifts (tap), drifts past touch
                // slop or gets consumed (scroll / checkbox — drop it), or runs
                // into the long-press timeout, which switches to drag mode. The
                // final pass reveals consumption done by ancestors for the same
                // event, such as the list scrolling.
                val isTap = withTimeoutOrNull(viewConfiguration.longPressTimeoutMillis) {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Main)
                        val change = event.changes.firstOrNull { it.id == down.id }
                        if (change == null || change.changedToUpIgnoreConsumed()) {
                            val finalEvent = awaitPointerEvent(PointerEventPass.Final)
                            return@withTimeoutOrNull change != null &&
                                event.changes.none { it.isConsumed } &&
                                finalEvent.changes.none { it.isConsumed }
                        }
                        if (event.changes.any { it.isConsumed }) {
                            return@withTimeoutOrNull false
                        }
                        if ((change.position - downPosition).getDistance() > viewConfiguration.touchSlop) {
                            return@withTimeoutOrNull false
                        }
                        if (awaitPointerEvent(PointerEventPass.Final).changes.any { it.isConsumed }) {
                            return@withTimeoutOrNull false
                        }
                    }
                    @Suppress("UNREACHABLE_CODE")
                    false
                }

                when (isTap) {
                    null -> {
                        // Long press: the entry toggles once and the drag
                        // direction comes from its new state. Every entry the
                        // finger crosses afterwards gets a plain toggle.
                        dragState.start(entryId, selectedIds()) { id -> onToggle(id) }
                        try {
                            drag(down.id) { change ->
                                val origin = dragState.boundsOf(entryId)
                                if (origin != null) {
                                    dragState.hitTest(origin.topLeft + change.position)
                                        ?.let { hovered -> dragState.moveTo(hovered) { id -> onToggle(id) } }
                                }
                                change.consume()
                            }
                        } finally {
                            dragState.reset()
                        }
                    }

                    true -> onTap(entryId)
                    false -> Unit
                }
            }
        }
}
