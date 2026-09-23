package com.diva.app.features.library.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.features.library.presentation.ui.screen.LibraryScreen

fun EntryProviderScope<NavKey>.libraryNav() {
    entry<LibraryRoute> {
        LibraryScreen()
    }
}