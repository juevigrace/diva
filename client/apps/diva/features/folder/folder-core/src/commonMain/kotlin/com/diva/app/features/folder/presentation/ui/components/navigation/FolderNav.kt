package com.diva.app.features.folder.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.features.folder.presentation.ui.screen.FolderScreen

fun EntryProviderScope<NavKey>.folderNav() {
    entry<FolderRoute> {
        FolderScreen()
    }
}