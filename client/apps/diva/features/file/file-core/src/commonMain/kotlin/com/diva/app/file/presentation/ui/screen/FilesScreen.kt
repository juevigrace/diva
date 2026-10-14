package com.diva.app.file.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.app.file.presentation.state.DirectoryState
import com.diva.app.file.presentation.ui.components.DirectoriesScaffold
import com.diva.app.file.presentation.viewmodel.DirectoryViewModel
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.files
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * The files tab root. Titled after the tab itself because at the root there is no
 * directory to name, and [DirectoryState.title] falls back to the same string.
 */
@Composable
fun FilesScreen(
    viewModel: DirectoryViewModel = koinViewModel(),
) {
    val state: DirectoryState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEnter(directoryId = null)
    }

    DirectoriesScaffold(
        title = stringResource(Res.string.files),
        state = state,
        onEvent = viewModel::onEvent,
    )
}

/**
 * A single directory pushed on top of the files tab. Titled after the directory being
 * browsed; back is handled by the nav host's predictive/gesture back.
 */
@Composable
fun DirectoryDetailScreen(
    directoryId: String,
    viewModel: DirectoryViewModel = koinViewModel(),
) {
    val state: DirectoryState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEnter(directoryId = directoryId)
    }

    DirectoriesScaffold(
        title = state.title,
        state = state,
        onEvent = viewModel::onEvent,
    )
}
