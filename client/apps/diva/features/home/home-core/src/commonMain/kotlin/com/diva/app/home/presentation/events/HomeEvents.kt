package com.diva.app.home.presentation.events

import com.diva.app.media.models.Media

sealed interface HomeEvents {
    data object OnBack : HomeEvents

    data class OnOpenMedia(val media: Media) : HomeEvents

    data object OnOpenSearch : HomeEvents

    data object OnOpenLibrary : HomeEvents
}