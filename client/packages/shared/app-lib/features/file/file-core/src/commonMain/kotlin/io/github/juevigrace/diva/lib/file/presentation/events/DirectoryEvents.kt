package io.github.juevigrace.diva.lib.file.presentation.events

import io.github.juevigrace.diva.lib.file.models.FileCategory

sealed interface DirectoryEvents {
    data class OnOpenDirectory(val directoryId: String) : DirectoryEvents

    data class OnNavigateToBreadcrumb(val directoryId: String) : DirectoryEvents

    data object OnNavigateUp : DirectoryEvents

    data class OnSetCategory(val category: FileCategory?) : DirectoryEvents

    /** Adds [entryId] to the selection or removes it when already selected. */
    data class OnToggleSelect(val entryId: String) : DirectoryEvents

    /** Selects every entry currently visible under the active category filter. */
    data object OnSelectAll : DirectoryEvents

    data object OnClearSelection : DirectoryEvents

    /** User agreed to the permission rationale dialog. */
    data object OnPermissionConfirm : DirectoryEvents

    /** User declined the permission rationale dialog. */
    data object OnPermissionDismiss : DirectoryEvents
}
