package io.github.juevigrace.diva.lib.file.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.lib.file.presentation.ui.screen.FilesScreen

fun EntryProviderScope<NavKey>.fileNav() {
    entry<FilesRoute> {
        FilesScreen()
    }
}
