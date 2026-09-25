package com.diva.app.features.media.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.features.media.presentation.ui.screen.MediaScreen

fun EntryProviderScope<NavKey>.mediaNav() {
    entry<MediaRoute> {
        MediaScreen()
    }
}