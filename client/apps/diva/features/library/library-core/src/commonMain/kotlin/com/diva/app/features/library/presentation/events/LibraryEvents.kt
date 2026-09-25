package com.diva.app.features.library.presentation.events

sealed interface LibraryEvents {
    data object OnBack : LibraryEvents
}