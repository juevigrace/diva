package com.diva.app.folder.presentation.viewmodel

import com.diva.app.folder.presentation.events.FolderEvents
import com.diva.app.folder.presentation.state.CollectionDraft
import com.diva.app.folder.presentation.state.FolderState
import com.diva.app.folder.presentation.state.folderBreadcrumbs
import com.diva.app.folder.presentation.state.folderEntries
import com.diva.app.folder.presentation.ui.components.navigation.FolderRoute
import com.diva.app.player.presentation.ui.components.navigation.PlayerRoute
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FolderViewModel(
    private val navigator: Navigator,
    private val tabNavigator: TabNavigator,
) : DivaViewModel() {

    // TODO(ui-pass): restore repository wiring
    private val scopes = mutableMapOf<String, FolderState>()

    val state: StateFlow<FolderState>
        field = MutableStateFlow(openScope(null))

    fun onEvent(event: FolderEvents) {
        when (event) {
            is FolderEvents.OnOpenFolder -> tabNavigator.navigate(FolderRoute(event.folderId))
            is FolderEvents.OnOpenFile -> navigator.navigate(
                PlayerRoute(mediaType = event.media.mediaType)
            )
            is FolderEvents.OnNavigateToBreadcrumb -> tabNavigator.popUntil(
                FolderRoute(event.folderId)
            )
            is FolderEvents.OnToggleSelection -> toggle(event.entryId)
            FolderEvents.OnStartSelection -> Unit
            FolderEvents.OnClearSelection -> update { it.copy(selectedIds = emptySet()) }
            FolderEvents.OnStartCollectionDraft -> startDraft()
            is FolderEvents.OnDraftNameChange -> updateDraft { it.copy(name = event.name) }
            is FolderEvents.OnDraftTypeChange -> updateDraft { it.copy(type = event.type) }
            FolderEvents.OnDismissCollectionDraft -> update { it.copy(draft = null) }
            FolderEvents.OnConfirmCollectionDraft -> confirmDraft()
        }
    }

    /**
     * Called when the folders tab becomes visible or a new [FolderRoute] is pushed.
     * Only switches when the id actually changed, so an in-progress selection in the
     * current directory is never clobbered by a recomposition.
     */
    fun onEnter(folderId: String?) {
        val key = folderId.orEmpty()
        val currentKey = state.value.currentFolderId.orEmpty()
        if (key == currentKey) return
        scopes[currentKey] = state.value
        state.value = scopes.getOrPut(key) { openScope(folderId) }
    }

    private fun toggle(entryId: String) {
        update { current ->
            val selected = if (entryId in current.selectedIds) {
                current.selectedIds - entryId
            } else {
                current.selectedIds + entryId
            }
            current.copy(selectedIds = selected)
        }
    }

    private fun startDraft() {
        val current = state.value
        val selected = current.entries.filter { it.id in current.selectedIds }
        val suggested = when {
            selected.isEmpty() -> ""
            selected.size == 1 -> selected.first().name.substringBeforeLast('.')
            else -> "${selected.size} items"
        }
        update { it.copy(draft = CollectionDraft(name = suggested)) }
    }

    private fun confirmDraft() {
        // TODO(ui-pass): create the collection via CollectionRepository, resolving each
        // selected directory through getMediaByFolder and adding the flattened media.
        update { it.copy(draft = null, selectedIds = emptySet()) }
    }

    private fun update(block: (FolderState) -> FolderState) {
        state.update(block)
        scopes[state.value.currentFolderId.orEmpty()] = state.value
    }

    private fun updateDraft(block: (CollectionDraft) -> CollectionDraft) {
        update { current -> current.copy(draft = current.draft?.let(block)) }
    }

    private fun openScope(folderId: String?): FolderState = FolderState(
        currentFolderId = folderId,
        breadcrumbs = folderBreadcrumbs(folderId),
        entries = folderEntries(folderId.orEmpty()),
    )
}