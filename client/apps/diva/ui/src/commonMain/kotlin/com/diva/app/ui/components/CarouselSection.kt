package com.diva.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.juevigrace.diva.lib.ui.components.carousel.Carousel

/**
 * A titled section that lays [items] out in a horizontally paged [Carousel],
 * computing a responsive card width from the available space.
 *
 * Generic over the item type: the [content] slot receives each item together with
 * the width modifier it should be rendered at. Header [title]/[subtitle] are slots
 * so each screen can style them.
 */
@Composable
fun <T> CarouselSection(
    items: List<T>,
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    spacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp),
    minCardWidth: Dp = 96.dp,
    maxCardWidth: Dp = 140.dp,
    content: @Composable (item: T, modifier: Modifier) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        title?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 8.dp),
            ) {
                it()
            }
        }
        subtitle?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                it()
            }
        }

        if (items.isNotEmpty()) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val layoutDirection = LocalLayoutDirection.current
                val gutter = contentPadding.calculateLeftPadding(layoutDirection) +
                    contentPadding.calculateRightPadding(layoutDirection)
                val visible = maxOf(
                    1,
                    ((maxWidth - gutter + spacing) / (maxCardWidth + spacing)).toInt(),
                )
                val cardWidth = ((maxWidth - gutter - spacing * (visible - 1)) / visible)
                    .coerceIn(minCardWidth, maxCardWidth)

                Carousel(
                    pageCount = items.size,
                    visiblePages = visible,
                    pageSize = cardWidth,
                    pageSpacing = spacing,
                    contentPadding = contentPadding,
                    modifier = Modifier.fillMaxWidth(),
                ) { page ->
                    content(items[page], Modifier.width(cardWidth))
                }
            }
        }
    }
}
