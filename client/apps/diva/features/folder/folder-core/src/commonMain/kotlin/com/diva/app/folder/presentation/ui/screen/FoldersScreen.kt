package com.diva.app.folder.presentation.ui.screen

import androidx.compose.runtime.Composable
import com.diva.app.folder.presentation.events.FolderEvents
import com.diva.app.folder.presentation.state.FolderState
import com.diva.app.folder.presentation.ui.components.FoldersScaffold
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.folders
import org.jetbrains.compose.resources.stringResource

/**
 * The folders tab root. Titled after the tab itself because at the root there is no
 * directory to name, and [FolderState.title] falls back to the same string.
 */
@Composable
fun FoldersScreen(
    state: FolderState,
    onEvent: (FolderEvents) -> Unit,
) {
    FoldersScaffold(
        title = stringResource(Res.string.folders),
        onBack = null,
        state = state,
        onEvent = onEvent,
    )
}

/**
 * A single directory pushed on top of the folders tab. Titled after the directory being
 * browsed and given a back affordance, since [onBack] pops the tab stack.
 */
@Composable
fun FolderDetailScreen(
    state: FolderState,
    onEvent: (FolderEvents) -> Unit,
    onBack: () -> Unit,
) {
    FoldersScaffold(
        title = state.title,
        onBack = onBack,
        state = state,
        onEvent = onEvent,
    )
}
