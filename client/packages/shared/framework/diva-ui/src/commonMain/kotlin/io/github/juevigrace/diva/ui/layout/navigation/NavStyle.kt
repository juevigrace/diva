package io.github.juevigrace.diva.ui.layout.navigation

import androidx.compose.runtime.staticCompositionLocalOf

enum class NavStyle {
    BottomBar,
    ModalDrawer,
    PermanentDrawer,
    Rail,
}

val LocalNavStyle = staticCompositionLocalOf<NavStyle> { error("No NavStyle provided") }
