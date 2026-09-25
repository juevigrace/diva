package com.diva.app.folder.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.folder.presentation.ui.screen.FolderScreen

fun EntryProviderScope<NavKey>.folderNav() {
    entry<FolderRoute> {
        FolderScreen()
    }
}