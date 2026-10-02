package com.diva.app.folder.presentation.events

import com.diva.app.folder.presentation.state.CollectionDraftType
import com.diva.app.media.models.Media

sealed interface FolderEvents {
    data class OnOpenFolder(val folderId: String) : FolderEvents

    data class OnOpenFile(val media: Media) : FolderEvents

    data class OnNavigateToBreadcrumb(val folderId: String) : FolderEvents

    data class OnToggleSelection(val entryId: String) : FolderEvents

    data object OnStartSelection : FolderEvents

    data object OnClearSelection : FolderEvents

    data object OnStartCollectionDraft : FolderEvents

    data class OnDraftNameChange(val name: String) : FolderEvents

    data class OnDraftTypeChange(val type: CollectionDraftType) : FolderEvents

    data object OnDismissCollectionDraft : FolderEvents

    data object OnConfirmCollectionDraft : FolderEvents
}