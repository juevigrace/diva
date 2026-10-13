package com.diva.app.folder.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.folder.presentation.ui.screen.FolderDetailScreen
import com.diva.app.folder.presentation.ui.screen.FoldersScreen

fun EntryProviderScope<NavKey>.folderNav() {
    entry<FoldersRoute> {
        FoldersScreen()
    }
    entry<FolderRoute> { key ->
        FolderDetailScreen(
            folderId = key.folderId,
        )
    }
}
