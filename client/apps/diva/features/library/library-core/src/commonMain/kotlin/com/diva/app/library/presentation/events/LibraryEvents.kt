package com.diva.app.library.presentation.events

import com.diva.app.library.presentation.state.LibraryFilter
import com.diva.app.media.models.Media

sealed interface LibraryEvents {
    data class OnFilterChange(val filter: LibraryFilter) : LibraryEvents

    data class OnToggleFavorite(val media: Media) : LibraryEvents

    data class OnOpenMedia(val media: Media) : LibraryEvents

    data class OnOpenFolder(val folderId: String) : LibraryEvents
}