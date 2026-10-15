package com.diva.app.file.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.file.presentation.ui.screen.FilesScreen

fun EntryProviderScope<NavKey>.fileNav() {
    entry<FilesRoute> {
        FilesScreen()
    }
}
