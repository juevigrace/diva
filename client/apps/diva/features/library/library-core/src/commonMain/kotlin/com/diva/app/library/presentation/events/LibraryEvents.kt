package com.diva.app.library.presentation.events

sealed interface LibraryEvents {
    data object OnBack : LibraryEvents
}