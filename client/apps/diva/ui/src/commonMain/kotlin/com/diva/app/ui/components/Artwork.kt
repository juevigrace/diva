package com.diva.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle

object ArtworkDefaults {
    val gradients: List<Pair<Color, Color>> = listOf(
        Color(0xFF00696E) to Color(0xFF9CF0F6),
        Color(0xFF8B4A63) to Color(0xFFFFD9E4),
        Color(0xFF00303A) to Color(0xFF4FB6C4),
        Color(0xFF3F2A56) to Color(0xFFB79BEE),
        Color(0xFF7A3410) to Color(0xFFFFB784),
        Color(0xFF1F4B2C) to Color(0xFF7BD389),
        Color(0xFF6B1E3A) to Color(0xFFF08AA8),
        Color(0xFF2E3A8F) to Color(0xFF94A6F5),
        Color(0xFF553B12) to Color(0xFFD7AE5C),
        Color(0xFF00505B) to Color(0xFF7FD8E4),
    )
}

/**
 * The gradient + initial fallback shown when no artwork image is available.
 *
 * [alt] drives both the gradient selection and the initial, and is meant to be the
 * title or description of the item the artwork belongs to.
 */
@Composable
fun ArtworkPlaceholder(
    alt: String,
    modifier: Modifier = Modifier,
    gradients: List<Pair<Color, Color>> = ArtworkDefaults.gradients,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    textStyle: TextStyle = LocalTextStyle.current,
    autoSize: TextAutoSize? = TextAutoSize.StepBased(
        minFontSize = MaterialTheme.typography.labelMedium.fontSize,
        maxFontSize = MaterialTheme.typography.displayMedium.fontSize,
    ),
    contentAlignment: Alignment = Alignment.Center,
) {
    val seed = alt.ifBlank { "?" }
    val (start, end) = gradients[seed.hashCode().mod(gradients.size)]
    val initial = seed.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Box(
        modifier = modifier.background(Brush.linearGradient(listOf(start, end))),
        contentAlignment = contentAlignment,
    ) {
        Text(
            text = initial,
            color = contentColor,
            style = textStyle,
            autoSize = autoSize,
        )
    }
}

/**
 * A general-purpose thumbnail renderer for media and collections.
 *
 * When [image] is provided it is drawn (clipped to [shape]) in place of the gradient
 * + initial [placeholder]. The renderer is loader-agnostic: supply a slot backed by
 * whatever image source you use (e.g. a network image loader or a local painter).
 *
 * @param alt The item title/description, used as the fallback initial and gradient seed.
 * @param image Optional slot drawing the item's assigned image.
 * @param placeholder Fallback shown when [image] is null; defaults to [ArtworkPlaceholder].
 */
@Composable
fun Artwork(
    alt: String,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    image: (@Composable BoxScope.() -> Unit)? = null,
    placeholder: @Composable BoxScope.() -> Unit = {
        ArtworkPlaceholder(alt, modifier = Modifier.fillMaxSize())
    },
) {
    Box(
        modifier = modifier.clip(shape),
        contentAlignment = Alignment.Center,
    ) {
        if (image != null) {
            image()
        } else {
            placeholder()
        }
    }
}
