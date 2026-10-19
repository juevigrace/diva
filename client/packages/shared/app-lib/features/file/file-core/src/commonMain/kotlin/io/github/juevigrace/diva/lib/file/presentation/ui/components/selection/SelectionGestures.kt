package io.github.juevigrace.diva.lib.file.presentation.ui.components.selection

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

/** Distance from the top/bottom edge of the list that starts auto-scrolling. */
private const val EDGE_ZONE_DP = 64f

/** Fastest auto-scroll step, applied at the very edge of the list. */
private const val MAX_SCROLL_DP = 18f

/**
 * Row-level half of the drag selection: it only publishes the row's bounds in
 * root coordinates so the container can hit-test a finger against them, and
 * drops them when the row leaves composition.
 */
@Composable
fun Modifier.dragSelectionBounds(entryId: String, dragState: DragSelectionState): Modifier {
    DisposableEffect(entryId) {
        onDispose { dragState.removeBounds(entryId) }
    }

    return this.onGloballyPositioned { dragState.updateBounds(entryId, it.boundsInRoot()) }
}

/**
 * Container-level gesture wiring for the files list.
 *
 * The gesture lives on the list rather than on each row because the rows are
 * recycled: dragging towards an edge scrolls the list, which would dispose the
 * row that started the drag and take the gesture down with it. From here a
 * single detector can also keep the band in sync while the list scrolls by
 * itself.
 *
 * A single detector drives all three interaction modes:
 *
 * - A plain tap is forwarded to [onTap]; the screen decides whether it opens a
 *   directory or toggles selection.
 * - Holding past the long-press timeout starts a drag selection: the entry under
 *   the finger is toggled once, which fixes the drag mode, and the band between
 *   it and the row under the finger is forced to that mode — dragging back over
 *   entries this drag selected unselects them again.
 * - Holding on the top or bottom edge keeps scrolling the list, feeding the
 *   freshly revealed rows to the same band. That loop runs outside the pointer
 *   scope, which is restricted and cannot scroll the list.
 *
 * A press a descendant already claimed (a checkbox tap) or that turns into a
 * scroll is left alone, so selection gestures never fight the list.
 */
@Composable
fun Modifier.dragSelectionContainer(
    dragState: DragSelectionState,
    listState: LazyListState,
    orderedIds: () -> List<String>,
    selectedIds: () -> Set<String>,
    onTap: (String) -> Unit,
    onToggle: (String) -> Unit,
): Modifier {
    val latestOrderedIds by rememberUpdatedState(orderedIds)
    val latestSelectedIds by rememberUpdatedState(selectedIds)
    val latestOnTap by rememberUpdatedState(onTap)
    val latestOnToggle by rememberUpdatedState(onToggle)

    // Bounds of the list itself, so node-local pointer positions can be mapped
    // onto the rows' root-coordinate bounds.
    var containerBounds by remember { mutableStateOf(Rect.Zero) }
    val density = LocalDensity.current
    val edgeZone = with(density) { EDGE_ZONE_DP.dp.toPx() }
    val maxScroll = with(density) { MAX_SCROLL_DP.dp.toPx() }

    LaunchedEffect(listState, edgeZone, maxScroll) {
        snapshotFlow { dragState.dragging }
            .collectLatest { dragging ->
                if (!dragging) return@collectLatest
                while (true) {
                    withFrameNanos { }
                    val finger = dragState.fingerPosition ?: continue
                    val delta = autoScrollStep(containerBounds, finger, edgeZone, maxScroll) ?: continue
                    if (listState.scrollBy(delta) != 0f) {
                        // The rows moved under a stationary finger, so the band
                        // has to be recomputed from their new bounds.
                        dragState.moveTo(containerBounds.topLeft + finger, latestOnToggle)
                    }
                }
            }
    }

    return this
        .onGloballyPositioned { containerBounds = it.boundsInRoot() }
        .pointerInput(Unit) {
            awaitEachGesture {
                // requireUnconsumed: a checkbox press is consumed by the
                // checkbox itself, so the list never sees that down.
                val down = awaitFirstDown()
                val downPosition = down.position
                val anchorId = dragState.hitTest(containerBounds.topLeft + downPosition)
                if (anchorId == null) return@awaitEachGesture

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
                    true -> latestOnTap(anchorId)

                    false -> Unit

                    null -> {
                        val started = dragState.start(
                            position = containerBounds.topLeft + downPosition,
                            orderedIds = latestOrderedIds(),
                            selectedIds = latestSelectedIds(),
                            onToggle = latestOnToggle,
                        )
                        if (!started) return@awaitEachGesture

                        var completed = false
                        try {
                            dragState.fingerPosition = downPosition
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                if (change.changedToUpIgnoreConsumed()) {
                                    completed = true
                                    break
                                }
                                dragState.fingerPosition = change.position
                                dragState.moveTo(containerBounds.topLeft + change.position, latestOnToggle)
                                // Consumed during the initial pass so the list
                                // does not scroll along with the drag.
                                change.consume()
                            }
                        } finally {
                            // Lifting keeps the band; anything else (the pointer
                            // was taken away, or the gesture died with the list)
                            // rolls the selection back to where it started.
                            if (completed) dragState.finish() else dragState.cancel(latestOnToggle)
                        }
                    }
                }
            }
        }
}

/**
 * Scroll step for a finger [position] (node-local) resting inside the top or
 * bottom [edgeZone] of [containerBounds], or null when it is in the middle.
 * Grows with how far the finger pushed past the edge, up to [maxScroll].
 */
private fun autoScrollStep(
    containerBounds: Rect,
    position: Offset,
    edgeZone: Float,
    maxScroll: Float,
): Float? {
    if (containerBounds.height <= edgeZone * 2f) return null
    val bottomEdge = containerBounds.height - edgeZone
    return when {
        position.y < edgeZone -> -maxScroll * ((edgeZone - position.y) / edgeZone).coerceIn(0f, 1f)
        position.y > bottomEdge -> maxScroll * ((position.y - bottomEdge) / edgeZone).coerceIn(0f, 1f)
        else -> null
    }
}
