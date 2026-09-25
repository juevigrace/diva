package com.diva.app.playlist.presentation.events

sealed interface PlaylistEvents {
    data object OnBack : PlaylistEvents
}