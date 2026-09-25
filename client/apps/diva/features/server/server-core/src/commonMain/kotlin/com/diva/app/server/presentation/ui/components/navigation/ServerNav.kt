package com.diva.app.server.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.server.presentation.ui.screen.ServerScreen

fun EntryProviderScope<NavKey>.serverNav() {
    entry<ServerRoute> {
        ServerScreen()
    }
}