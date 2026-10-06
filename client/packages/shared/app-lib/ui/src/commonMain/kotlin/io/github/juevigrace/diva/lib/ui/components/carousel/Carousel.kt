package io.github.juevigrace.diva.lib.ui.components.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private enum class Edge { Start, End }

private val Arrows = 32.dp
private val ArrowGutter = 4.dp

/**
 * Horizontally paged row with edge arrows.
 *
 * Wheel scrolling is deliberately not handled: the row lives inside a vertical
 * scroll container, so intercepting wheel events stole the page scroll and
 * broke it. Paging is left to the touch gesture the pager already has, plus
 * click-and-drag and arrow buttons on pointer platforms. See [CarouselDefaults]
 * for the per-platform defaults.
 *
 * @param pageCount total number of pages.
 * @param visiblePages how many pages fit at once, used only to decide whether
 *   the arrows are needed.
 * @param pageSize width of a single page.
 * @param contentPadding padding applied outside the first and last page.
 * @param pageSpacing gap between pages.
 * @param arrowsEnabled show the edge arrows, defaulting to the platform
 *   preference.
 * @param dragScrollEnabled enable click-and-drag panning, defaulting to the
 *   platform preference.
 */
@Composable
fun Carousel(
    pageCount: Int,
    visiblePages: Int,
    modifier: Modifier = Modifier,
    pageSize: Dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp),
    pageSpacing: Dp = 12.dp,
    arrowsEnabled: Boolean = CarouselDefaults.arrowsEnabled,
    dragScrollEnabled: Boolean = CarouselDefaults.dragScrollEnabled,
    state: PagerState = rememberPagerState { pageCount },
    content: @Composable (Int) -> Unit,
) {
    if (pageCount <= 0) return

    val scope = rememberCoroutineScope()
    val pagerModifier = if (dragScrollEnabled) {
        modifier.dragScroll(state, scope)
    } else {
        modifier
    }

    Box(modifier = modifier) {
        HorizontalPager(
            state = state,
            contentPadding = contentPadding,
            pageSpacing = pageSpacing,
            pageSize = PageSize.Fixed(pageSize),
            modifier = pagerModifier,
        ) { page ->
            content(page)
        }

        if (arrowsEnabled && pageCount > visiblePages) {
            CarouselArrow(
                edge = Edge.Start,
                enabled = state.canScrollBackward,
                onClick = {
                    scope.launch { state.animateScrollToPage(state.currentPage - 1) }
                },
            )
            CarouselArrow(
                edge = Edge.End,
                enabled = state.canScrollForward,
                onClick = {
                    scope.launch { state.animateScrollToPage(state.currentPage + 1) }
                },
            )
        }
    }
}

/**
 * The arrow sits on top of the pager, so it carries the only clickable in its
 * subtree. That stops the press from also reaching the page underneath, which
 * would otherwise navigate while the arrow scrolled.
 */
@Composable
private fun BoxScope.CarouselArrow(edge: Edge, enabled: Boolean, onClick: () -> Unit) {
    val container = if (enabled) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val content = if (enabled) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .align(
                if (edge == Edge.Start) Alignment.CenterStart else Alignment.CenterEnd
            )
            .padding(horizontal = ArrowGutter)
            .size(Arrows)
            .clip(CircleShape)
            .background(container)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = when (edge) {
                Edge.Start -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
                Edge.End -> Icons.AutoMirrored.Filled.KeyboardArrowRight
            },
            contentDescription = if (edge == Edge.Start) "Previous" else "Next",
            tint = content,
            modifier = Modifier.size(18.dp),
        )
    }
}

/**
 * Click-and-drag panning for pointer platforms, which the pager does not
 * provide on its own.
 *
 * Only horizontal drags are consumed, and only after the gesture has passed the
 * drag threshold, so a vertical drag still reaches the enclosing vertical
 * scroll container.
 */
private fun Modifier.dragScroll(
    state: PagerState,
    scope: CoroutineScope,
): Modifier = pointerInput(state) {
    detectHorizontalDragGestures(
        onHorizontalDrag = { change, amount ->
            change.consume()
            scope.launch { state.scrollBy(-amount) }
        },
    )
}