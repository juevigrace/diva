package io.github.juevigrace.diva.ui.layout.navigation

import androidx.compose.runtime.staticCompositionLocalOf

enum class NavItemStyle {
    Rail,
    Drawer,
}

val LocalNavItemStyle = staticCompositionLocalOf { NavItemStyle.Drawer }
