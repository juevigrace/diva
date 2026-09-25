package com.diva.app.playlist.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.playlist.presentation.ui.screen.PlaylistScreen

fun EntryProviderScope<NavKey>.playlistNav() {
    entry<PlaylistRoute> {
        PlaylistScreen()
    }
}