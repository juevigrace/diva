package io.github.juevigrace.diva.lib.file.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.lib.file.presentation.actions.FileAction
import io.github.juevigrace.diva.lib.file.presentation.ui.screen.FilesScreen

/**
 * Hosts the files browser. Consumers can hand in [actions] to run against the
 * entries selected in the screen; the file module itself declares none.
 */
fun EntryProviderScope<NavKey>.fileNav(actions: List<FileAction> = emptyList()) {
    entry<FilesRoute> {
        FilesScreen(actions = actions)
    }
}
