package com.diva.app.folder.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.app.folder.presentation.state.FolderState
import com.diva.app.folder.presentation.ui.components.FoldersScaffold
import com.diva.app.folder.presentation.viewmodel.FolderViewModel
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.folders
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * The folders tab root. Titled after the tab itself because at the root there is no
 * directory to name, and [FolderState.title] falls back to the same string.
 */
@Composable
fun FoldersScreen(
    viewModel: FolderViewModel = koinViewModel(),
) {
    val state: FolderState by viewModel.state.collectAsStateWithLifecycle()
    FoldersScaffold(
        title = stringResource(Res.string.folders),
        state = state,
        onEvent = viewModel::onEvent,
    )
}

/**
 * A single directory pushed on top of the folders tab. Titled after the directory being
 * browsed; back is handled by the nav host's predictive/gesture back.
 */
@Composable
fun FolderDetailScreen(
    folderId: String,
    viewModel: FolderViewModel = koinViewModel(),
) {
    val state: FolderState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEnter(folderId = folderId)
    }

    FoldersScaffold(
        title = state.title,
        state = state,
        onEvent = viewModel::onEvent,
    )
}
