package com.diva.app.file.presentation.events

import com.diva.app.file.models.FileCategory

sealed interface DirectoryEvents {
    data class OnOpenDirectory(val directoryId: String) : DirectoryEvents

    data class OnNavigateToBreadcrumb(val directoryId: String) : DirectoryEvents

    data object OnNavigateUp : DirectoryEvents

    data class OnSetCategory(val category: FileCategory?) : DirectoryEvents

    /** User agreed to the permission rationale dialog. */
    data object OnPermissionConfirm : DirectoryEvents

    /** User declined the permission rationale dialog. */
    data object OnPermissionDismiss : DirectoryEvents
}
