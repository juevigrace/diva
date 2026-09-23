package com.diva.app.features.collection.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.features.collection.presentation.ui.screen.CollectionScreen

fun EntryProviderScope<NavKey>.albumNav() {
    entry<AlbumRoute> {
        CollectionScreen()
    }
}