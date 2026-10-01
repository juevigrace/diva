package com.diva.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PLACEHOLDER_GRADIENTS = listOf(
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

@Composable
fun Artwork(
    seed: String,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    contentDescription: String? = null,
) {
    val (start, end) = PLACEHOLDER_GRADIENTS[
        seed.hashCode().mod(PLACEHOLDER_GRADIENTS.size)
    ]
    val initial = seed.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    BoxWithConstraints(
        modifier = modifier.clip(shape),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(start, end))),
        )
        if (maxWidth > 24.dp) {
            Text(
                text = initial,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontSize = (maxWidth.value * 0.36f).sp,
            )
        }
    }
}