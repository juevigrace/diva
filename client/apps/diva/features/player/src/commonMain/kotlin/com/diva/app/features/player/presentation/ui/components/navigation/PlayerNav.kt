package com.diva.app.features.player.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.features.player.presentation.ui.screen.PlayerScreen

fun EntryProviderScope<NavKey>.playerNav() {
    entry<PlayerRoute> {
        PlayerScreen()
    }
}