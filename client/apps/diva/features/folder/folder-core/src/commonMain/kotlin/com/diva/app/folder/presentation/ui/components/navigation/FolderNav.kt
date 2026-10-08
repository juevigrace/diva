package com.diva.app.folder.presentation.ui.components.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.folder.presentation.ui.components.FolderDetailScreen
import com.diva.app.folder.presentation.ui.components.FoldersScreen
import com.diva.app.folder.presentation.viewmodel.FolderViewModel
import io.github.juevigrace.diva.ui.navigation.LocalNavigator
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.folderNav() {
    entry<FoldersRoute> {
        val viewModel: FolderViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        FoldersScreen(
            state = state,
            onEvent = viewModel::onEvent,
        )
    }
    entry<FolderRoute> { key ->
        val viewModel: FolderViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val navigator = LocalNavigator.current
        viewModel.onEnter(key.folderId)
        FolderDetailScreen(
            state = state,
            onEvent = viewModel::onEvent,
            onBack = { navigator.pop() },
        )
    }
}
