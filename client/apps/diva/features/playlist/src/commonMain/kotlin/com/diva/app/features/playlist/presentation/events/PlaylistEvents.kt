package com.diva.app.features.playlist.presentation.events

sealed interface PlaylistEvents {
    data object OnBack : PlaylistEvents
}