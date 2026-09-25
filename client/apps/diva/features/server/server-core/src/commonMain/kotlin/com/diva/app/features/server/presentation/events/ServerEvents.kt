package com.diva.app.features.server.presentation.events

sealed interface ServerEvents {
    data object OnBack : ServerEvents
}