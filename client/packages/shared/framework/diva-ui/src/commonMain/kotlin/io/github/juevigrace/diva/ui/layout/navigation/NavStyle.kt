package io.github.juevigrace.diva.ui.layout.navigation

import androidx.compose.runtime.staticCompositionLocalOf

enum class NavStyle {
    BottomBar,
    ModalDrawer,
    PermanentDrawer,
    Rail,
}

/**
 * Style of the screen currently being composed. Screen composables install it for every slot they
 * own, so a lambda reads the same style whether it lives in a bar, the nav surface or the content.
 *
 * Defaults to [NavStyle.BottomBar] so a plain Screen used on its own stays well defined instead of
 * throwing for a missing provider.
 */
val LocalNavStyle = staticCompositionLocalOf { NavStyle.BottomBar }
