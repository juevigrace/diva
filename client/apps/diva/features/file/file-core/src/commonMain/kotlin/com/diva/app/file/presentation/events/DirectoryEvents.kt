package com.diva.app.file.presentation.events

sealed interface DirectoryEvents {
    data class OnOpenDirectory(val directoryId: String) : DirectoryEvents

    data class OnNavigateToBreadcrumb(val directoryId: String) : DirectoryEvents

    data object OnNavigateUp : DirectoryEvents
}
