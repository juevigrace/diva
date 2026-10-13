package io.github.juevigrace.diva.ui.layout

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

/**
 * Mutable styling state for the navigation surface rendered by
 * [AdaptiveScreen].
 *
 * Create it with [rememberNavSurfaceState] and pass it to [AdaptiveScreen].
 * Every field is readable and writable, so the appearance can be changed at
 * runtime from anywhere holding the reference, the same way [androidx.compose.material3.DrawerState]
 * or a lazy list state is used.
 *
 * A `null` field means "use the Material default for the active
 * [io.github.juevigrace.diva.ui.layout.navigation.NavStyle]"; defaults are
 * resolved on every recomposition, so theme changes still propagate. A
 * non-null field always wins. The drawer fields serve both the modal and the
 * permanent drawer, each resolving its own default; [gesturesEnabled] and
 * [scrimColor] only apply to the modal drawer, and the rail fields only to the
 * rail.
 */
@Stable
class NavSurfaceState(
    gesturesEnabled: Boolean = true,
    scrimColor: Color? = null,
    drawerShape: Shape? = null,
    drawerContainerColor: Color? = null,
    drawerContentColor: Color? = null,
    drawerTonalElevation: Dp? = null,
    drawerWindowInsets: WindowInsets? = null,
    railContainerColor: Color? = null,
    railContentColor: Color? = null,
    railHeader: (@Composable ColumnScope.() -> Unit)? = null,
    railWindowInsets: WindowInsets? = null,
) {
    var gesturesEnabled by mutableStateOf(gesturesEnabled)
    var scrimColor by mutableStateOf(scrimColor)
    var drawerShape by mutableStateOf(drawerShape)
    var drawerContainerColor by mutableStateOf(drawerContainerColor)
    var drawerContentColor by mutableStateOf(drawerContentColor)
    var drawerTonalElevation by mutableStateOf(drawerTonalElevation)
    var drawerWindowInsets by mutableStateOf(drawerWindowInsets)
    var railContainerColor by mutableStateOf(railContainerColor)
    var railContentColor by mutableStateOf(railContentColor)
    var railHeader by mutableStateOf(railHeader)
    var railWindowInsets by mutableStateOf(railWindowInsets)
}

/**
 * Remembers a [NavSurfaceState] whose fields start at the given values and
 * can be mutated afterward. Pass the result to [AdaptiveScreen].
 */
@Composable
fun rememberNavSurfaceState(
    gesturesEnabled: Boolean = true,
    scrimColor: Color? = null,
    drawerShape: Shape? = null,
    drawerContainerColor: Color? = null,
    drawerContentColor: Color? = null,
    drawerTonalElevation: Dp? = null,
    drawerWindowInsets: WindowInsets? = null,
    railContainerColor: Color? = null,
    railContentColor: Color? = null,
    railHeader: (@Composable ColumnScope.() -> Unit)? = null,
    railWindowInsets: WindowInsets? = null,
): NavSurfaceState = remember {
    NavSurfaceState(
        gesturesEnabled = gesturesEnabled,
        scrimColor = scrimColor,
        drawerShape = drawerShape,
        drawerContainerColor = drawerContainerColor,
        drawerContentColor = drawerContentColor,
        drawerTonalElevation = drawerTonalElevation,
        drawerWindowInsets = drawerWindowInsets,
        railContainerColor = railContainerColor,
        railContentColor = railContentColor,
        railHeader = railHeader,
        railWindowInsets = railWindowInsets,
    )
}
