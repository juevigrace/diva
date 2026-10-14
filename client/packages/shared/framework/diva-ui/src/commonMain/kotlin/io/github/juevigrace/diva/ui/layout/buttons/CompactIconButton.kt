package io.github.juevigrace.diva.ui.layout.buttons

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

/**
 * Icon button that skips the 48dp minimum touch target
 * [androidx.compose.material3.IconButton] enforces, so the footprint is exactly
 * what the caller sets through [modifier] and the size of [icon].
 *
 * Same parameter shape as [androidx.compose.material3.IconButton] but with
 * [shape] and [contentPadding] exposed; [contentPadding] defaults to 0.dp so
 * nothing is added around the icon unless the caller asks for it.
 *
 * Colors come from [colors] (defaults to [IconButtonDefaults.iconButtonColors]):
 * the resolved content color is provided to [icon], and the container color is
 * drawn behind it, clipped to [shape].
 *
 * @param onClick invoked when the button is tapped.
 * @param icon slot rendering the icon.
 * @param enabled whether the button responds to taps; also selects the
 * disabled colors from [colors].
 * @param colors icon and container colors, resolved against [enabled].
 * @param shape clip and container shape.
 * @param contentPadding padding inside the clickable area, around [icon].
 * @param interactionSource for observing press/hover states; a source is
 * remembered when null.
 */
@Composable
fun CompactIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    shape: Shape = CircleShape,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    interactionSource: MutableInteractionSource? = null,
    icon: @Composable () -> Unit,
) {
    val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val container = if (enabled) colors.containerColor else colors.disabledContainerColor
    val content = if (enabled) colors.contentColor else colors.disabledContentColor
    CompositionLocalProvider(LocalContentColor provides content) {
        Box(
            modifier = modifier
                .clip(shape)
                .background(container)
                .clickable(
                    interactionSource = resolvedInteractionSource,
                    indication = LocalIndication.current,
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(contentPadding),
            contentAlignment = Alignment.Center,
        ) {
            icon()
        }
    }
}
