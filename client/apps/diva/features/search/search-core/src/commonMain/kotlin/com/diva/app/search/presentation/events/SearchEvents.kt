package com.diva.app.search.presentation.events

import com.diva.app.media.models.Media

sealed interface SearchEvents {
    data class OnQueryChange(val query: String) : SearchEvents

    data class OnSelectSuggestion(val suggestion: String) : SearchEvents

    data object OnClear : SearchEvents

    data class OnOpenMedia(val media: Media) : SearchEvents

    data class OnOpenCollection(val collectionId: String) : SearchEvents

    data class OnOpenFolder(val folderId: String) : SearchEvents
}