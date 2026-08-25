package io.github.juevigrace.diva.ui.window

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class WindowInfo(
    val width: Dp,
    val height: Dp,
) {
    val orientation: ScreenOrientation
        get() = if (width < height) ScreenOrientation.Portrait else ScreenOrientation.Landscape

    val isPortrait: Boolean
        get() = orientation == ScreenOrientation.Portrait

    val isLandscape: Boolean
        get() = orientation == ScreenOrientation.Landscape

    // classified by the smaller dimension, mirroring master's orientation-aware thresholds
    val size: ScreenSize
        get() {
            val minDimension = if (width < height) width else height
            return when {
                minDimension < 600.dp -> ScreenSize.Small
                minDimension < 840.dp -> ScreenSize.Medium
                minDimension < 1200.dp -> ScreenSize.Large
                else -> ScreenSize.XLarge
            }
        }

    val widthSizeClass: WindowWidthSizeClass
        get() = when {
            width < 600.dp -> WindowWidthSizeClass.Compact
            width < 840.dp -> WindowWidthSizeClass.Medium
            else -> WindowWidthSizeClass.Expanded
        }
}

@Composable
fun rememberWindowInfo(): WindowInfo {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    return remember(windowInfo.containerSize, density) {
        with(density) {
            WindowInfo(
                width = windowInfo.containerSize.width.toDp(),
                height = windowInfo.containerSize.height.toDp(),
            )
        }
    }
}
